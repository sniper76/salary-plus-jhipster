package com.salary.plus.service;

import com.salary.plus.domain.Shop;
import com.salary.plus.enums.ShopType;
import com.salary.plus.repository.ShopRepository;
import com.salary.plus.service.dto.AdminShopDTO;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
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
public class ShopService {

    private static final Logger LOG = LoggerFactory.getLogger(ShopService.class);

    private final ShopRepository shopRepository;

    public Shop create(AdminShopDTO adminShopDTO, String login) {
        Shop shop = new Shop();
        shop.setNameKo(adminShopDTO.getNameKo());
        shop.setNameEn(adminShopDTO.getNameEn());
        shop.setType(ShopType.fromValue(adminShopDTO.getType()));
        shop.setWorkStartTime(adminShopDTO.getWorkStartTime());
        shop.created(login);
        return shopRepository.save(shop);
    }

    @Transactional(readOnly = true)
    public Page<AdminShopDTO> getAllManagedShopsForPage(Pageable pageable) {
        return shopRepository.findAll(pageable).map(AdminShopDTO::new);
    }

    @Transactional(readOnly = true)
    public List<AdminShopDTO> getAllManagedShops() {
        return shopRepository.findAll().stream().map(AdminShopDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public Optional<Shop> get(long id) {
        return shopRepository.findById(id);
    }

    public Optional<Shop> update(AdminShopDTO adminShopDTO, String login) {
        final Shop shop = get(adminShopDTO.getId()).orElseThrow();
        shop.setNameKo(adminShopDTO.getNameKo());
        shop.setNameEn(adminShopDTO.getNameEn());
        shop.setType(ShopType.fromValue(adminShopDTO.getType()));
        shop.setWorkStartTime(adminShopDTO.getWorkStartTime());
        shop.updateLastModified(login);

        return Optional.of(shopRepository.save(shop));
    }

    public void deleteShop(long id) {
        get(id).ifPresent(shopRepository::delete);
    }

    public List<Shop> getAllActivated() {
        return shopRepository.findAllByActivated(true);
    }
}
