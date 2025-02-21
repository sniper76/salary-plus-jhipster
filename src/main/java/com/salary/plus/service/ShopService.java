package com.salary.plus.service;

import com.salary.plus.domain.Shop;
import com.salary.plus.domain.ShopUserMapping;
import com.salary.plus.domain.User;
import com.salary.plus.repository.ShopRepository;
import com.salary.plus.repository.ShopUserMappingRepository;
import com.salary.plus.service.dto.AdminShopDTO;
import com.salary.plus.service.mapper.UserMapper;
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
public class ShopService {

    private static final Logger LOG = LoggerFactory.getLogger(ShopService.class);

    private final ShopRepository shopRepository;
    private final ShopUserMappingRepository shopUserMappingRepository;
    private final UserService userService;
    private final UserMapper userMapper;

    public Shop createShop(AdminShopDTO adminShopDTO, String login) {
        Shop shop = new Shop();
        shop.setName(adminShopDTO.getName());
        shop.setCreatedBy(login);
        final Shop savedShop = shopRepository.save(shop);
        extracted(adminShopDTO, savedShop.getId());
        return savedShop;
    }

    private void extracted(AdminShopDTO adminShopDTO, Long shopId) {
        if (adminShopDTO.getUsers() == null) {
            return;
        }
        adminShopDTO
            .getUsers()
            .stream()
            .forEach(userDTO -> {
                final User user = userService.createUser(userDTO);
                final ShopUserMapping userMapping = new ShopUserMapping();
                userMapping.setUserId(user.getId());
                userMapping.setShopId(shopId);
                shopUserMappingRepository.save(userMapping);
            });
    }
}
