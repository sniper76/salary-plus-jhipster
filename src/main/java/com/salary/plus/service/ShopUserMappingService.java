package com.salary.plus.service;

import com.salary.plus.domain.Shop;
import com.salary.plus.domain.ShopUserMapping;
import com.salary.plus.domain.User;
import com.salary.plus.repository.ShopUserMappingRepository;
import java.util.List;
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

    @Transactional(readOnly = true)
    public List<User> getMappingUsersByShopId(Long shopId) {
        return shopUserMappingRepository.findAllUserByShopId(shopId);
    }

    @Transactional(readOnly = true)
    public List<User> getMappingUsersByShopIdAndCommissionTargetUser(Long shopId) {
        return shopUserMappingRepository.findAllUserByShopIdAndCommissionTargetUser(shopId, true);
    }

    @Transactional(readOnly = true)
    public List<ShopUserMapping> getAllByUserId(Long userId) {
        return shopUserMappingRepository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Shop> getMappingShops(String username) {
        return shopUserMappingRepository.findAllShopByLogin(username);
    }

    public ShopUserMapping save(ShopUserMapping userMapping) {
        return shopUserMappingRepository.save(userMapping);
    }

    public void deleteByUserIdAndShopId(Long userId, Long shopId) {
        shopUserMappingRepository.deleteByUserIdAndShopId(userId, shopId);
    }
}
