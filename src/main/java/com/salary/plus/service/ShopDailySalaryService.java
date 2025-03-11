package com.salary.plus.service;

import com.salary.plus.domain.ShopUserDailySalary;
import com.salary.plus.domain.User;
import com.salary.plus.repository.ShopUserDailySalaryRepository;
import com.salary.plus.service.dto.ShopDailySalaryDTO;
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
public class ShopDailySalaryService {

    private static final Logger LOG = LoggerFactory.getLogger(ShopDailySalaryService.class);

    private final ShopUserDailySalaryRepository shopDailySalaryRepository;
    private final ShopUserMappingService shopUserMappingService;

    public List<ShopUserDailySalary> create(ShopDailySalaryDTO userDTO) {
        final Integer salary = 0; //salary 조회
        if (CollectionUtils.isEmpty(userDTO.getUserIds())) {
            userDTO.setUserIds(shopUserMappingService.getMappingUsersByShopId(userDTO.getShopId()).stream().map(User::getId).toList());
        }
        return userDTO
            .getUserIds()
            .stream()
            .map(item -> {
                ShopUserDailySalary shopSalesItem = new ShopUserDailySalary();
                shopSalesItem.setShopId(userDTO.getShopId());
                shopSalesItem.setDate(userDTO.getDate());
                shopSalesItem.setUserId(item);
                shopSalesItem.setPrice(salary);
                return shopDailySalaryRepository.save(shopSalesItem);
            })
            .toList();
    }
}
