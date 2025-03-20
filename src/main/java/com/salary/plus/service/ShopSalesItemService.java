package com.salary.plus.service;

import com.salary.plus.domain.ShopSalesItem;
import com.salary.plus.repository.ShopSalesItemRepository;
import com.salary.plus.service.dto.ShopSalesItemDTO;
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
public class ShopSalesItemService {

    private static final Logger LOG = LoggerFactory.getLogger(ShopSalesItemService.class);

    private final ShopSalesItemRepository shopSalesItemRepository;

    public ShopSalesItem create(Long shopId, ShopSalesItemDTO userDTO) {
        ShopSalesItem shopSalesItem = new ShopSalesItem();
        shopSalesItem.setShopId(shopId);
        shopSalesItem.setNameKo(userDTO.getNameKo());
        shopSalesItem.setNameEn(userDTO.getNameEn());
        shopSalesItem.setCommissionTarget(userDTO.isCommissionTarget());
        shopSalesItem.setSnack(userDTO.isSnack());
        shopSalesItem.setPrice(userDTO.getPrice());
        shopSalesItem.setShopCommissionPrice(userDTO.getShopCommissionPrice());
        shopSalesItem.setMamaCommissionPrice(userDTO.getMamaCommissionPrice());
        shopSalesItem.setModelCommissionPrice(userDTO.getModelCommissionPrice());
        return shopSalesItemRepository.save(shopSalesItem);
    }

    public Optional<ShopSalesItem> update(Long shopId, ShopSalesItemDTO userDTO) {
        return Optional.of(get(shopId, userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(it -> {
                it.setShopId(shopId);
                it.setNameKo(userDTO.getNameKo());
                it.setNameEn(userDTO.getNameEn());
                it.setPrice(userDTO.getPrice());
                it.setActivated(userDTO.isActivated());
                it.setCommissionTarget(userDTO.isCommissionTarget());
                it.setSnack(userDTO.isSnack());
                it.setShopCommissionPrice(userDTO.getShopCommissionPrice());
                it.setMamaCommissionPrice(userDTO.getMamaCommissionPrice());
                it.setModelCommissionPrice(userDTO.getModelCommissionPrice());
                return shopSalesItemRepository.save(it);
            });
    }

    public void delete(Long shopId, long salesItemId) {
        get(shopId, salesItemId).ifPresent(shopSalesItemRepository::delete);
    }

    @Transactional(readOnly = true)
    public List<ShopSalesItem> getAllSalesItems(Long shopId) {
        return shopSalesItemRepository.findAllByShopIdAndActivated(shopId, true);
    }

    @Transactional(readOnly = true)
    public Page<ShopSalesItem> getAllSalesItems(Long shopId, Pageable pageable) {
        return shopSalesItemRepository.findAllByShopId(shopId, pageable);
    }

    @Transactional(readOnly = true)
    public Optional<ShopSalesItem> get(Long shopId, long salesItemId) {
        return shopSalesItemRepository.findByIdAndShopIdAndActivated(salesItemId, shopId, true);
    }
}
