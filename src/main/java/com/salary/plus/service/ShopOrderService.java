package com.salary.plus.service;

import com.salary.plus.domain.ShopOrder;
import com.salary.plus.repository.ShopOrderRepository;
import com.salary.plus.service.dto.ShopOrderDTO;
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

    @Transactional(readOnly = true)
    public List<ShopOrderResponse> getAllOrdersByDate(Long shopId, String date) {
        return shopOrderRepository.findAllByShopIdAndDateAndActivated(shopId, date, true);
    }
}
