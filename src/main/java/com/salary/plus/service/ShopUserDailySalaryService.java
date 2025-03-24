package com.salary.plus.service;

import com.salary.plus.domain.ShopBaseSalary;
import com.salary.plus.domain.ShopUserDailySalary;
import com.salary.plus.domain.User;
import com.salary.plus.repository.ShopBaseSalaryRepository;
import com.salary.plus.repository.ShopUserDailySalaryRepository;
import com.salary.plus.service.dto.ShopDailySalaryDTO;
import com.salary.plus.web.rest.errors.BadRequestAlertException;
import java.util.List;
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
        final ShopBaseSalary mapping = shopBaseSalaryRepository
            .findByShopIdAndUserId(shopId, userId, true)
            .orElseThrow(() -> new BadRequestAlertException("Not found base salary", "works", "work.not.found.base.salary"));
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

    public void createAllCheckIn(Long shopId, String date) {
        final List<User> users = shopUserMappingService.getMappingUsersByShopIdAndCommissionTargetUser(shopId);
        users.forEach(it -> {
            createCheckIn(shopId, date, it.getId());
        });
    }
}
