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

    public ShopSalesItem create(Long shopId, ShopSalesItemDTO shopSalesItemDTO) {
        ShopSalesItem shopSalesItem = new ShopSalesItem();
        shopSalesItem.setShopId(shopId);
        shopSalesItem.setNameKo(shopSalesItemDTO.getNameKo());
        shopSalesItem.setNameEn(shopSalesItemDTO.getNameEn());
        shopSalesItem.setCommissionTargetYn(shopSalesItemDTO.isCommissionTargetYn());
        shopSalesItem.setSnackYn(shopSalesItemDTO.isSnackYn());
        shopSalesItem.setPrice(shopSalesItemDTO.getPrice());
        shopSalesItem.setShopCommissionPrice(shopSalesItemDTO.getShopCommissionPrice());
        shopSalesItem.setMamaCommissionPrice(shopSalesItemDTO.getMamaCommissionPrice());
        shopSalesItem.setModelCommissionPrice(shopSalesItemDTO.getModelCommissionPrice());
        shopSalesItem.created(shopSalesItemDTO.getLogin());
        return shopSalesItemRepository.save(shopSalesItem);
    }

    public Optional<ShopSalesItem> update(Long shopId, ShopSalesItemDTO shopSalesItemDTO) {
        return Optional.of(get(shopId, shopSalesItemDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(it -> {
                it.setShopId(shopId);
                it.setNameKo(shopSalesItemDTO.getNameKo());
                it.setNameEn(shopSalesItemDTO.getNameEn());
                it.setPrice(shopSalesItemDTO.getPrice());
                it.setActivated(shopSalesItemDTO.isActivated());
                it.setCommissionTargetYn(shopSalesItemDTO.isCommissionTargetYn());
                it.setSnackYn(shopSalesItemDTO.isSnackYn());
                it.setShopCommissionPrice(shopSalesItemDTO.getShopCommissionPrice());
                it.setMamaCommissionPrice(shopSalesItemDTO.getMamaCommissionPrice());
                it.setModelCommissionPrice(shopSalesItemDTO.getModelCommissionPrice());
                it.updateLastModified(shopSalesItemDTO.getLogin());
                return shopSalesItemRepository.save(it);
            });
    }

    public void delete(Long shopId, long salesItemId) {
        get(shopId, salesItemId).ifPresent(shopSalesItemRepository::delete);
    }

    public void updateActivated(Long shopId, long salesItemId, String login) {
        get(shopId, salesItemId).ifPresent(it -> {
            it.setActivated(false);
            it.updateLastModified(login);
            shopSalesItemRepository.save(it);
        });
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
