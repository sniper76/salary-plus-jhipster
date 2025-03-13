package com.salary.plus.service;

import com.salary.plus.domain.ShopOrder;
import com.salary.plus.domain.ShopOrderDetail;
import com.salary.plus.domain.ShopUserSalesSalary;
import com.salary.plus.repository.ShopOrderDetailRepository;
import com.salary.plus.repository.ShopOrderRepository;
import com.salary.plus.repository.ShopUserDailySalaryRepository;
import com.salary.plus.repository.ShopUserSalesSalaryRepository;
import com.salary.plus.service.dto.ShopOrderCreateDTO;
import com.salary.plus.service.dto.ShopOrderResponse;
import java.util.List;
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
public class ShopOrderService {

    private static final Logger LOG = LoggerFactory.getLogger(ShopOrderService.class);

    private final ShopOrderRepository shopOrderRepository;
    private final ShopOrderDetailRepository shopOrderDetailRepository;
    private final ShopUserSalesSalaryRepository shopUserSalesSalaryRepository;

    @Transactional(readOnly = true)
    public List<ShopOrderResponse> getAllOrdersByDate(Long shopId, String date) {
        return shopOrderRepository.findAllByShopIdAndDateAndActivated(shopId, date, true);
    }

    public void createOrder(ShopOrderCreateDTO shopOrderCreateDTO) {
        final Long shopId = shopOrderCreateDTO.getShopId();
        final String date = shopOrderCreateDTO.getDate();
        final Long tableId = shopOrderCreateDTO.getTableId();
        final List<Long> salesItemIds = shopOrderCreateDTO.getSalesItemIds();
        final List<Long> modelIds = shopOrderCreateDTO.getModelIds();
        final List<Integer> prices = shopOrderCreateDTO.getPrices();
        ShopOrder shopOrder = new ShopOrder();
        shopOrder.setShopId(shopId);
        shopOrder.setShopTableId(tableId);
        shopOrder.setDate(date);
        shopOrder.setTotalPrice(prices.stream().mapToInt(e -> e).sum());
        shopOrderRepository.save(shopOrder);
        for (int k = 0; k < salesItemIds.size(); k++) {
            ShopOrderDetail shopOrderDetail = new ShopOrderDetail();
            shopOrderDetail.setShopOrderId(shopOrder.getId());
            shopOrderDetail.setShopSalesItemId(salesItemIds.get(k));
            shopOrderDetail.setPrice(prices.get(k));
            shopOrderDetailRepository.save(shopOrderDetail);
            if (getLongId(modelIds, k) != null) {
                ShopUserSalesSalary shopUserSalesSalary = new ShopUserSalesSalary();
                shopUserSalesSalary.setShopOrderDetailId(shopOrderDetail.getId());
                shopUserSalesSalary.setPrice(prices.get(k));
                shopUserSalesSalary.setUserId(modelIds.get(k));
                shopUserSalesSalaryRepository.save(shopUserSalesSalary);
            }
        }
    }

    private Long getLongId(List<Long> list, int k) {
        try {
            return list.get(k);
        } catch (IndexOutOfBoundsException ioe) {
            return null;
        }
    }
}
