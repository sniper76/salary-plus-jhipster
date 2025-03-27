package com.salary.plus.service;

import com.salary.plus.domain.ShopOrder;
import com.salary.plus.domain.ShopOrderDetail;
import com.salary.plus.domain.ShopOrderDetailDiscount;
import com.salary.plus.domain.ShopUserSalesSalary;
import com.salary.plus.repository.ShopOrderDetailDiscountRepository;
import com.salary.plus.repository.ShopOrderDetailRepository;
import com.salary.plus.repository.ShopOrderRepository;
import com.salary.plus.repository.ShopUserSalesSalaryRepository;
import com.salary.plus.service.dto.ShopOrderCreateDTO;
import com.salary.plus.service.dto.ShopOrderDetailResponse;
import com.salary.plus.service.dto.ShopOrderDetailWithDiscountResponse;
import com.salary.plus.service.dto.ShopOrderResponse;
import com.salary.plus.web.rest.errors.BadRequestAlertException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
    private final ShopOrderDetailDiscountRepository shopOrderDetailDiscountRepository;

    @Transactional(readOnly = true)
    public List<ShopOrderResponse> getAllOrdersByDate(Long shopId, String date) {
        final List<ShopOrderResponse> orders = shopOrderRepository.findAllByShopIdAndDateAndActivated(shopId, date, true);
        orders.forEach(it -> {
            it.setDiscountResponseList(shopOrderDetailDiscountRepository.findAllByOrderId(it.getOrderId()));
        });
        return orders;
    }

    public void createOrder(Long shopId, String date, ShopOrderCreateDTO shopOrderCreateDTO) {
        final Long tableId = shopOrderCreateDTO.getTableId();
        final List<Long> salesItemIds = shopOrderCreateDTO.getSalesItemIds();
        final List<Long> modelIds = shopOrderCreateDTO.getModelIds();
        final List<Integer> prices = shopOrderCreateDTO.getPrices();
        ShopOrder shopOrder = new ShopOrder();
        shopOrder.setShopId(shopId);
        shopOrder.setShopTableId(tableId);
        shopOrder.setDate(date);
        shopOrder.setTotalPrice(prices.stream().mapToInt(e -> e).sum());
        shopOrder.created(shopOrderCreateDTO.getLogin());
        shopOrderRepository.save(shopOrder);
        for (int k = 0; k < salesItemIds.size(); k++) {
            ShopOrderDetail shopOrderDetail = new ShopOrderDetail();
            shopOrderDetail.setShopOrderId(shopOrder.getId());
            shopOrderDetail.setShopSalesItemId(salesItemIds.get(k));
            shopOrderDetail.setPrice(prices.get(k));
            shopOrderDetail.created(shopOrderCreateDTO.getLogin());
            shopOrderDetailRepository.save(shopOrderDetail);
            if (getLongId(modelIds, k) != null) {
                ShopUserSalesSalary shopUserSalesSalary = new ShopUserSalesSalary();
                shopUserSalesSalary.setShopOrderDetailId(shopOrderDetail.getId());
                shopUserSalesSalary.setPrice(prices.get(k));
                shopUserSalesSalary.setUserId(modelIds.get(k));
                shopUserSalesSalary.created(shopOrderCreateDTO.getLogin());
                shopUserSalesSalaryRepository.save(shopUserSalesSalary);
            }
        }
    }

    public void updateOrder(Long shopId, String date, ShopOrderCreateDTO shopOrderCreateDTO) {
        //order table change??
        //order total price change
        //order detail price change
        //user sales change
        final Long tableId = shopOrderCreateDTO.getTableId();
        final Long orderId = shopOrderCreateDTO.getOrderId();
        final List<Long> orderDetailIds = shopOrderCreateDTO.getOrderDetailIds();
        final List<Long> salesItemIds = shopOrderCreateDTO.getSalesItemIds();
        final List<Long> modelIds = shopOrderCreateDTO.getModelIds();
        final List<Integer> prices = shopOrderCreateDTO.getPrices();
        final ShopOrder shopOrder = shopOrderRepository
            .findByIdAndShopIdAndDate(orderId, shopId, date)
            .orElseThrow(() -> new BadRequestAlertException("Not found target", "orders", "order.not.found.target"));
        if (shopOrder.isPaid()) {
            throw new BadRequestAlertException("Already paid order", "orders", "order.already.paid.order");
        }
        shopOrder.setShopTableId(tableId);
        shopOrder.setTotalPrice(prices.stream().mapToInt(e -> e).sum());
        shopOrder.updateLastModified(shopOrderCreateDTO.getLogin());
        shopOrderRepository.save(shopOrder);
        List<ShopDetailOrderData> dataList = buildData(orderDetailIds, salesItemIds, modelIds, prices);
        for (final ShopDetailOrderData orderData : dataList) {
            if (orderData.orderDetailId == null) {
                //new insert
                ShopOrderDetail shopOrderDetail = new ShopOrderDetail();
                shopOrderDetail.setShopOrderId(orderId);
                shopOrderDetail.setShopSalesItemId(orderData.salesItemId());
                shopOrderDetail.setPrice(orderData.price());
                shopOrderDetail.created(shopOrderCreateDTO.getLogin());
                shopOrderDetailRepository.save(shopOrderDetail);
                if (orderData.modelId() != null) {
                    ShopUserSalesSalary shopUserSalesSalary = new ShopUserSalesSalary();
                    shopUserSalesSalary.setShopOrderDetailId(shopOrderDetail.getId());
                    shopUserSalesSalary.setPrice(orderData.price());
                    shopUserSalesSalary.setUserId(orderData.modelId());
                    shopUserSalesSalary.created(shopOrderCreateDTO.getLogin());
                    shopUserSalesSalaryRepository.save(shopUserSalesSalary);
                }
            } else {
                final ShopOrderDetail shopOrderDetail = shopOrderDetailRepository
                    .findByIdAndShopOrderId(orderData.orderDetailId, orderId)
                    .orElseThrow(() -> new BadRequestAlertException("Not found order detail", "orders", "order.not.found.target"));
                shopOrderDetail.setPrice(orderData.price());
                shopOrderDetail.setShopSalesItemId(orderData.salesItemId());
                shopOrderDetail.updateLastModified(shopOrderCreateDTO.getLogin());
                shopOrderDetailRepository.save(shopOrderDetail);
                if (orderData.modelId() != null) {
                    final ShopUserSalesSalary shopUserSalesSalary = shopUserSalesSalaryRepository
                        .findByShopOrderDetailId(shopOrderDetail.getId())
                        .orElseThrow(() -> new BadRequestAlertException("Not found order detail", "orders", "order.not.found.target"));
                    shopUserSalesSalary.updateLastModified(shopOrderCreateDTO.getLogin());
                    if (Objects.equals(shopUserSalesSalary.getUserId(), orderData.modelId())) {
                        shopUserSalesSalary.setPrice(orderData.price());
                        shopUserSalesSalaryRepository.save(shopUserSalesSalary);
                    } else {
                        shopUserSalesSalary.setActivated(false);
                        shopUserSalesSalaryRepository.save(shopUserSalesSalary);

                        // new insert
                        ShopUserSalesSalary newUserSalary = new ShopUserSalesSalary();
                        newUserSalary.setShopOrderDetailId(shopOrderDetail.getId());
                        newUserSalary.setUserId(orderData.modelId());
                        newUserSalary.setPrice(orderData.price());
                        newUserSalary.created(shopOrderCreateDTO.getLogin());
                        shopUserSalesSalaryRepository.save(newUserSalary);
                    }
                }
            }
        }
    }

    private List<ShopDetailOrderData> buildData(
        List<Long> orderDetailIds,
        List<Long> salesItemIds,
        List<Long> modelIdx,
        List<Integer> prices
    ) {
        List<ShopDetailOrderData> list = new ArrayList<>();
        for (int k = 0; k < salesItemIds.size(); k++) {
            Long salesItemId = salesItemIds.get(k);
            Integer price = prices.get(k);
            list.add(new ShopDetailOrderData(getLongId(orderDetailIds, k), salesItemId, getLongId(modelIdx, k), price));
        }
        return list;
    }

    public List<ShopOrderDetailWithDiscountResponse> getOrderDetailWithDiscounts(Long shopId, String date, Long orderId) {
        return shopOrderDetailRepository.findAllOrderDetailWithDiscounts(shopId, date, orderId);
    }

    public void createOrderWithDiscounts(Long shopId, String date, ShopOrderCreateDTO shopOrderCreateDTO) {
        final Long orderId = shopOrderCreateDTO.getOrderId();
        final List<Long> salesItemIds = shopOrderCreateDTO.getSalesItemIds();
        final List<Long> salesItemDiscountIds = shopOrderCreateDTO.getSalesItemDiscountIds();
        final List<Integer> prices = shopOrderCreateDTO.getPrices();
        final List<ShopOrderDetailResponse> orderDetails = getOrderDetails(shopId, date, orderId);
        updateDiscounts(shopOrderCreateDTO, salesItemIds, salesItemDiscountIds, orderDetails, prices);

        updateOrderPaid(shopId, date, orderId);
    }

    private void updateDiscounts(
        ShopOrderCreateDTO shopOrderCreateDTO,
        List<Long> salesItemIds,
        List<Long> salesItemDiscountIds,
        List<ShopOrderDetailResponse> orderDetails,
        List<Integer> prices
    ) {
        if (CollectionUtils.isEmpty(salesItemIds) || CollectionUtils.isEmpty(salesItemDiscountIds)) {
            return;
        }
        for (ShopOrderDetailResponse detailResponse : orderDetails) {
            for (int k = 0; k < salesItemIds.size(); k++) {
                final Long salesItemId = salesItemIds.get(k);
                if (Objects.equals(detailResponse.getId(), salesItemId)) {
                    final Long orderDetailId = detailResponse.getOrderDetailId();
                    ShopOrderDetailDiscount detailDiscount = new ShopOrderDetailDiscount();
                    detailDiscount.setShopOrderDetailId(orderDetailId);
                    detailDiscount.setShopSalesItemDiscountId(salesItemDiscountIds.get(k));
                    detailDiscount.setPrice(prices.get(k));
                    detailDiscount.created(shopOrderCreateDTO.getLogin());
                    shopOrderDetailDiscountRepository.save(detailDiscount);
                }
            }
        }
    }

    private record ShopDetailOrderData(Long orderDetailId, Long salesItemId, Long modelId, Integer price) {}

    private Long getLongId(List<Long> list, int k) {
        if (list == null) {
            return null; //바파인, LD 없는 경우
        }
        try {
            return list.get(k);
        } catch (IndexOutOfBoundsException ioe) {
            return null;
        }
    }

    public void updateOrderPaid(Long shopId, String date, Long orderId) {
        shopOrderRepository
            .findByIdAndShopIdAndDate(orderId, shopId, date)
            .ifPresent(it -> {
                it.setPaid(true);
                shopOrderRepository.save(it);
            });
    }

    public List<ShopOrderDetailResponse> getOrderDetails(Long shopId, String date, Long orderId) {
        return shopOrderRepository.findAllByIdAndShopIdAndDate(orderId, shopId, date, true);
    }

    public Optional<ShopOrderDetail> getOrderDetail(Long orderId, Long orderDetailId) {
        return shopOrderDetailRepository.findByIdAndShopOrderId(orderDetailId, orderId);
    }

    public void deleteOrderDetail(ShopOrderDetail shopOrderDetail) {
        shopOrderDetailRepository.save(shopOrderDetail);
    }
}
