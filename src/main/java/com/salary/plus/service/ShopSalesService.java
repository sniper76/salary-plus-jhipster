package com.salary.plus.service;

import com.salary.plus.domain.ShopOrder;
import com.salary.plus.repository.ShopOrderDetailRepository;
import com.salary.plus.repository.ShopOrderRepository;
import com.salary.plus.service.dto.ShopOrderDetailDTO;
import com.salary.plus.service.dto.ShopOrderDetailResponse;
import com.salary.plus.service.dto.ShopSalesDTO;
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
public class ShopSalesService {

    private static final Logger LOG = LoggerFactory.getLogger(ShopSalesService.class);

    private final ShopOrderDetailRepository shopOrderDetailRepository;
    private final ShopOrderRepository shopOrderRepository;

    @Transactional(readOnly = true)
    public List<ShopSalesDTO> getAllSales(Long shopId, String startDate, String endDate) {
        return shopOrderDetailRepository.findAllByShopIdAndSearchDate(shopId, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public Page<ShopOrder> getSalesForPage(Long shopId, String date, Pageable pageable) {
        return shopOrderRepository.findAllByShopIdAndDate(shopId, date, pageable);
    }

    public List<ShopOrderDetailDTO> getSalesDetails(Long shopId, Long orderId) {
        return shopOrderDetailRepository.findAllByShopIdAndOrderId(shopId, orderId);
    }
}
