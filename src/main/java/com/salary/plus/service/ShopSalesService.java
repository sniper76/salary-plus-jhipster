package com.salary.plus.service;

import com.salary.plus.domain.ShopSalesItem;
import com.salary.plus.repository.ShopOrderDetailRepository;
import com.salary.plus.repository.ShopSalesItemRepository;
import com.salary.plus.service.dto.ShopSalesItemDTO;
import com.salary.plus.service.dto.record.ShopSalesDTO;
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

    @Transactional(readOnly = true)
    public List<ShopSalesDTO> getAllSales(Long shopId, String startDate, String endDate) {
        return shopOrderDetailRepository.findAllByShopIdAndSearchDate(shopId, startDate, endDate);
    }

    public Optional<ShopSalesDTO> get(Long shopId, String date) {
        return Optional.empty();
    }
}
