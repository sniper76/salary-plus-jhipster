package com.salary.plus.service;

import com.salary.plus.domain.ShopSalesItem;
import com.salary.plus.repository.ShopSalesItemRepository;
import com.salary.plus.repository.ShopTableRepository;
import com.salary.plus.service.dto.ShopSalesItemDTO;
import com.salary.plus.service.dto.ShopTableResponse;
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
public class ShopTableService {

    private static final Logger LOG = LoggerFactory.getLogger(ShopTableService.class);

    private final ShopTableRepository shopTableRepository;

    public List<ShopTableResponse> getAllTables(Long shopId) {
        return shopTableRepository.findAllByShopIdAndActivated(shopId, true);
    }
}
