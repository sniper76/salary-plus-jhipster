package com.salary.plus.service;

import com.salary.plus.domain.Shop;
import com.salary.plus.domain.ShopUserMapping;
import com.salary.plus.domain.User;
import com.salary.plus.enums.ShopType;
import com.salary.plus.repository.ShopRepository;
import com.salary.plus.repository.ShopUserMappingRepository;
import com.salary.plus.service.dto.AdminShopDTO;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
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
public class ShopUserMappingService {

    private static final Logger LOG = LoggerFactory.getLogger(ShopUserMappingService.class);

    private final ShopUserMappingRepository shopUserMappingRepository;

    public Optional<ShopUserMapping> getShopUserMappingByLogin(Long shopId, String login) {
        return shopUserMappingRepository.findByShopIdAndLogin(shopId, login);
    }
}
