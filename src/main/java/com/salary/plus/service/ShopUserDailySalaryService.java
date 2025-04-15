package com.salary.plus.service;

import com.salary.plus.domain.ShopBaseSalary;
import com.salary.plus.domain.ShopPenalty;
import com.salary.plus.domain.ShopUserDailySalary;
import com.salary.plus.domain.ShopUserPenaltyMapping;
import com.salary.plus.domain.User;
import com.salary.plus.enums.PenaltyType;
import com.salary.plus.repository.ShopBaseSalaryRepository;
import com.salary.plus.repository.ShopUserDailySalaryRepository;
import com.salary.plus.repository.ShopUserPenaltyMappingRepository;
import com.salary.plus.service.dto.ShopDailySalaryDTO;
import com.salary.plus.service.dto.ShopSalaryDTO;
import com.salary.plus.service.dto.ShopSalaryDetailDTO;
import com.salary.plus.service.dto.ShopSalesDTO;
import com.salary.plus.service.handler.PenaltyHandlerResolver;
import com.salary.plus.utils.DateUtils;
import com.salary.plus.web.rest.errors.BadRequestAlertException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing users.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ShopUserDailySalaryService {

    private static final Logger LOG = LoggerFactory.getLogger(ShopUserDailySalaryService.class);

    private final ShopUserDailySalaryRepository shopUserDailySalaryRepository;
    private final ShopUserMappingService shopUserMappingService;
    private final ShopBaseSalaryRepository shopBaseSalaryRepository;
    private final ShopPenaltyService shopPenaltyService;
    private final ShopUserPenaltyMappingRepository shopUserPenaltyMappingRepository;

    private final PenaltyHandlerResolver penaltyHandlerResolver;

    public List<ShopUserDailySalary> create(ShopDailySalaryDTO userDTO, String login) {
        final Integer salary = 0; //salary 조회
        if (CollectionUtils.isEmpty(userDTO.getUserIds())) {
            userDTO.setUserIds(
                shopUserMappingService.getMappingUsersByShopIdAndActivated(userDTO.getShopId()).stream().map(User::getId).toList()
            );
        }
        return userDTO
            .getUserIds()
            .stream()
            .map(item -> {
                return saveAndGetShopUserDailySalary(userDTO.getShopId(), userDTO.getDate(), item, salary, login);
            })
            .toList();
    }

    private ShopUserPenaltyMapping saveAndGetShopUserPenaltyMapping(
        Long shopPenaltyId,
        String date,
        Long userId,
        Integer price,
        String login
    ) {
        ShopUserPenaltyMapping shopUserDailySalary = new ShopUserPenaltyMapping();
        shopUserDailySalary.setShopPenaltyId(shopPenaltyId);
        shopUserDailySalary.setDate(date);
        shopUserDailySalary.setUserId(userId);
        shopUserDailySalary.setPrice(price);
        shopUserDailySalary.created(login);
        return shopUserPenaltyMappingRepository.save(shopUserDailySalary);
    }

    private ShopUserDailySalary saveAndGetShopUserDailySalary(Long shopId, String date, Long userId, Integer salary, String login) {
        ShopUserDailySalary shopUserDailySalary = new ShopUserDailySalary();
        shopUserDailySalary.setShopId(shopId);
        shopUserDailySalary.setDate(date);
        shopUserDailySalary.setUserId(userId);
        shopUserDailySalary.setPrice(salary);
        shopUserDailySalary.created(login);
        return shopUserDailySalaryRepository.save(shopUserDailySalary);
    }

    public void createCheckIn(Long shopId, String date, Long userId, String login) {
        final List<ShopPenalty> penaltyList = shopPenaltyService.getAllPenalties(shopId);
        deleteUserDailySalaryWithUserPenaltyMapping(shopId, date, userId, penaltyList);

        final ShopBaseSalary mapping = getShopBaseSalaryByUserId(shopId, userId);
        createCheckInLatePenalty(shopId, date, userId, login);
        shopUserDailySalaryRepository
            .findByShopIdAndDateAndUserIdAndActivated(shopId, date, userId, true)
            .ifPresentOrElse(
                it -> {
                    it.setPrice(mapping.getPrice());
                    it.updateLastModified(login);
                    shopUserDailySalaryRepository.save(it);
                },
                () -> {
                    saveAndGetShopUserDailySalary(shopId, date, userId, mapping.getPrice(), login);
                }
            );
    }

    private void createCheckInLatePenalty(Long shopId, String date, Long userId, String login) {
        final Optional<ShopPenalty> timePenalty = shopPenaltyService.getByType(shopId, PenaltyType.TIME);
        if (timePenalty.isPresent()) {
            final ShopPenalty shopPenalty = timePenalty.get();

            final Integer penaltyPrice = penaltyHandlerResolver.resolve(shopId, shopPenalty);
            shopUserPenaltyMappingRepository
                .findByShopPenaltyIdAndUserIdAndDate(shopPenalty.getId(), userId, date)
                .ifPresentOrElse(
                    it -> {
                        it.setPrice(penaltyPrice);
                        it.updateLastModified(login);
                        shopUserPenaltyMappingRepository.save(it);
                    },
                    () -> {
                        saveAndGetShopUserPenaltyMapping(shopPenalty.getId(), date, userId, penaltyPrice, login);
                    }
                );
        }
    }

    private ShopBaseSalary getShopBaseSalaryByUserId(Long shopId, Long userId) {
        return shopBaseSalaryRepository
            .findByShopIdAndUserId(shopId, userId, true)
            .orElseThrow(() -> new BadRequestAlertException("Not found base salary", "works", "work.not.found.base.salary"));
    }

    public void createAllCheckIn(Long shopId, String date, String login) {
        final List<User> users = shopUserMappingService.getMappingUsersByShopIdAndCommissionTargetUser(shopId);
        users.forEach(it -> {
            createCheckIn(shopId, date, it.getId(), login);
        });
    }

    public void updateAllHalfSalary(Long shopId, String date) {
        final List<ShopUserDailySalary> dailySalaries = shopUserDailySalaryRepository.findAllByShopIdAndDate(shopId, date);
        updateHalfSalaryByList(shopId, date, dailySalaries);
    }

    private void updateHalfSalaryByList(Long shopId, String date, List<ShopUserDailySalary> dailySalaries) {
        if (dailySalaries.isEmpty()) {
            throw new BadRequestAlertException("현재일 일당 정보를 찾을 수 없습니다", date, "work.not.found.user.daily.salary");
        }
        dailySalaries.forEach(it -> {
            final ShopBaseSalary mapping = getShopBaseSalaryByUserId(shopId, it.getUserId());
            it.setPrice(mapping.getPrice() / 2);
            shopUserDailySalaryRepository.save(it);
        });
    }

    public void updateHalfSalary(Long shopId, String date, Long userId) {
        final List<ShopUserDailySalary> dailySalaries = shopUserDailySalaryRepository.findAllByShopIdAndDateAndUserId(shopId, date, userId);
        updateHalfSalaryByList(shopId, date, dailySalaries);
    }

    @Deprecated
    @Transactional(readOnly = true)
    public List<ShopSalaryDTO> getAllDailySalaries(Long shopId, String startDate, String endDate) {
        return shopUserDailySalaryRepository.findAllByShopIdAndSearchDate(shopId, startDate, endDate);
    }

    public void createAbsence(Long shopId, String date, Long userId, String login) {
        final List<ShopPenalty> penaltyList = shopPenaltyService.getAllPenalties(shopId);
        deleteUserDailySalaryWithUserPenaltyMapping(shopId, date, userId, penaltyList);

        final String weekday = DateUtils.getWeekdayFormat();
        penaltyList
            .stream()
            .filter(it -> it.getTypeValue().contains(weekday))
            .forEach(elem -> {
                shopUserPenaltyMappingRepository
                    .findByShopPenaltyIdAndUserIdAndDate(elem.getId(), userId, date)
                    .ifPresentOrElse(
                        it -> {
                            it.setPrice(elem.getPrice());
                            it.updateLastModified(login);
                            shopUserPenaltyMappingRepository.save(it);
                        },
                        () -> {
                            saveAndGetShopUserPenaltyMapping(elem.getId(), date, userId, elem.getPrice(), login);
                        }
                    );
            });
    }

    private void deleteUserDailySalaryWithUserPenaltyMapping(Long shopId, String date, Long userId, List<ShopPenalty> penaltyList) {
        //user daily salary 가 있으면 삭제
        final List<ShopUserDailySalary> dailySalaries = shopUserDailySalaryRepository.findAllByShopIdAndDateAndUserId(shopId, date, userId);
        shopUserDailySalaryRepository.deleteAll(dailySalaries);
        //user penalty mapping 에 지각 패널티가 있으면 삭제
        penaltyList
            .stream()
            .filter(it -> PenaltyType.getDayWithTimePenalty().contains(it.getType()))
            .forEach(it -> {
                shopUserPenaltyMappingRepository
                    .findByShopPenaltyIdAndUserIdAndDate(it.getId(), userId, date)
                    .ifPresent(shopUserPenaltyMappingRepository::delete);
            });
    }

    public List<ShopSalaryDetailDTO> getAllDailySalariesForPage(Long shopId, Long userId, String startDate, String endDate) {
        return shopUserDailySalaryRepository.findAllByShopIdAndUserIdAndSearchDate(shopId, userId, startDate, endDate);
    }
}
