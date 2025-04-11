package com.salary.plus.service;

import com.salary.plus.domain.ShopBaseSalary;
import com.salary.plus.domain.ShopUserDailySalary;
import com.salary.plus.domain.User;
import com.salary.plus.repository.ShopBaseSalaryRepository;
import com.salary.plus.repository.ShopUserDailySalaryRepository;
import com.salary.plus.service.dto.ShopDailySalaryDTO;
import com.salary.plus.service.dto.ShopSalaryDTO;
import com.salary.plus.service.dto.ShopUserSalaryBaseMappingResponse;
import com.salary.plus.web.rest.errors.BadRequestAlertException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public List<ShopUserDailySalary> create(ShopDailySalaryDTO userDTO) {
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
                return getShopUserDailySalary(userDTO.getShopId(), userDTO.getDate(), item, salary);
            })
            .toList();
    }

    private ShopUserDailySalary getShopUserDailySalary(Long shopId, String date, Long userId, Integer salary) {
        ShopUserDailySalary shopUserDailySalary = new ShopUserDailySalary();
        shopUserDailySalary.setShopId(shopId);
        shopUserDailySalary.setDate(date);
        shopUserDailySalary.setUserId(userId);
        shopUserDailySalary.setPrice(salary);
        return shopUserDailySalaryRepository.save(shopUserDailySalary);
    }

    public void createCheckIn(Long shopId, String date, Long userId) {
        final ShopBaseSalary mapping = getShopBaseSalaryByUserId(shopId, userId);
        shopUserDailySalaryRepository
            .findByShopIdAndDateAndUserIdAndActivated(shopId, date, userId, true)
            .ifPresentOrElse(
                it -> {
                    it.setPrice(mapping.getPrice());
                    shopUserDailySalaryRepository.save(it);
                },
                () -> {
                    getShopUserDailySalary(shopId, date, userId, mapping.getPrice());
                }
            );
    }

    private ShopBaseSalary getShopBaseSalaryByUserId(Long shopId, Long userId) {
        return shopBaseSalaryRepository
            .findByShopIdAndUserId(shopId, userId, true)
            .orElseThrow(() -> new BadRequestAlertException("Not found base salary", "works", "work.not.found.base.salary"));
    }

    public void createAllCheckIn(Long shopId, String date) {
        final List<User> users = shopUserMappingService.getMappingUsersByShopIdAndCommissionTargetUser(shopId);
        users.forEach(it -> {
            createCheckIn(shopId, date, it.getId());
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

    @Transactional(readOnly = true)
    public List<ShopSalaryDTO> getAllDailySalaries(Long shopId, String startDate, String endDate) {
        return shopUserDailySalaryRepository.findAllByShopIdAndSearchDate(shopId, startDate, endDate);
    }
}
