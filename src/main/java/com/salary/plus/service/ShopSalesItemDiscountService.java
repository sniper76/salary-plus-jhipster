package com.salary.plus.service;

import com.salary.plus.domain.ShopSalesItemDiscount;
import com.salary.plus.enums.DiscountType;
import com.salary.plus.repository.ShopSalesItemDiscountRepository;
import com.salary.plus.service.dto.ShopSalesItemDiscountDTO;
import com.salary.plus.service.dto.ShopSalesItemDiscountResponse;
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
public class ShopSalesItemDiscountService {

    private static final Logger LOG = LoggerFactory.getLogger(ShopSalesItemDiscountService.class);

    private final ShopSalesItemDiscountRepository shopSalesItemDiscountRepository;

    public ShopSalesItemDiscount create(Long shopId, ShopSalesItemDiscountDTO shopSalesItemDiscountDTO) {
        ShopSalesItemDiscount shopSalesItemDiscount = new ShopSalesItemDiscount();
        shopSalesItemDiscount.setShopSalesItemId(shopSalesItemDiscountDTO.getSalesItemId());
        shopSalesItemDiscount.setNameKo(shopSalesItemDiscountDTO.getNameKo());
        shopSalesItemDiscount.setNameEn(shopSalesItemDiscountDTO.getNameEn());
        shopSalesItemDiscount.setType(DiscountType.fromValue(shopSalesItemDiscountDTO.getType()));
        shopSalesItemDiscount.setPrice(shopSalesItemDiscountDTO.getPrice());
        shopSalesItemDiscount.setShopCommissionPrice(shopSalesItemDiscountDTO.getShopCommissionPrice());
        shopSalesItemDiscount.setMamaCommissionPrice(shopSalesItemDiscountDTO.getMamaCommissionPrice());
        shopSalesItemDiscount.setModelCommissionPrice(shopSalesItemDiscountDTO.getModelCommissionPrice());
        shopSalesItemDiscount.created(shopSalesItemDiscountDTO.getLogin());
        return shopSalesItemDiscountRepository.save(shopSalesItemDiscount);
    }

    public Optional<ShopSalesItemDiscount> update(Long shopId, ShopSalesItemDiscountDTO shopSalesItemDiscountDTO) {
        return Optional.of(get(shopId, shopSalesItemDiscountDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(it -> {
                it.setShopSalesItemId(shopSalesItemDiscountDTO.getSalesItemId());
                it.setNameKo(shopSalesItemDiscountDTO.getNameKo());
                it.setNameEn(shopSalesItemDiscountDTO.getNameEn());
                it.setType(DiscountType.fromValue(shopSalesItemDiscountDTO.getType()));
                it.setPrice(shopSalesItemDiscountDTO.getPrice());
                it.setActivated(shopSalesItemDiscountDTO.isActivated());
                it.setShopCommissionPrice(shopSalesItemDiscountDTO.getShopCommissionPrice());
                it.setMamaCommissionPrice(shopSalesItemDiscountDTO.getMamaCommissionPrice());
                it.setModelCommissionPrice(shopSalesItemDiscountDTO.getModelCommissionPrice());
                it.updateLastModified(shopSalesItemDiscountDTO.getLogin());
                return shopSalesItemDiscountRepository.save(it);
            });
    }

    public void delete(Long shopId, long salesItemId) {
        get(shopId, salesItemId).ifPresent(shopSalesItemDiscountRepository::delete);
    }

    public void updateActivated(Long shopId, long salesItemId, String login) {
        get(shopId, salesItemId).ifPresent(it -> {
            it.setActivated(false);
            it.updateLastModified(login);
            shopSalesItemDiscountRepository.save(it);
        });
    }

    /**
     * 주문별 할인 대상 정보 리스트 조회
     * @param shopId
     * @param orderId
     * @return
     */
    @Transactional(readOnly = true)
    public List<ShopSalesItemDiscountResponse> getAllSalesItemDiscountsByOrderId(Long shopId, Long orderId) {
        return shopSalesItemDiscountRepository.findAllByShopIdAndOrderIdAndActivated(shopId, orderId, true);
    }

    @Transactional(readOnly = true)
    public List<ShopSalesItemDiscount> getAllSalesItemDiscounts(Long shopId) {
        return shopSalesItemDiscountRepository.findAllByShopIdAndActivated(shopId, true);
    }

    @Transactional(readOnly = true)
    public Page<ShopSalesItemDiscount> getAllSalesItemDiscounts(Long shopId, Pageable pageable) {
        return shopSalesItemDiscountRepository.findAllByShopId(shopId, pageable);
    }

    @Transactional(readOnly = true)
    public Optional<ShopSalesItemDiscount> get(Long shopId, long salesItemId) {
        return shopSalesItemDiscountRepository.findByIdAndActivated(salesItemId, true);
    }
}
