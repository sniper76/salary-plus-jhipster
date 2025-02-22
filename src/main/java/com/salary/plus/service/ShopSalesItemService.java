package com.salary.plus.service;

import com.salary.plus.domain.ShopSalesItem;
import com.salary.plus.domain.ShopUserMapping;
import com.salary.plus.repository.ShopSalesItemRepository;
import com.salary.plus.repository.ShopUserMappingRepository;
import com.salary.plus.service.dto.ShopSalesItemDTO;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
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
public class ShopSalesItemService {

    private static final Logger LOG = LoggerFactory.getLogger(ShopSalesItemService.class);

    private final ShopSalesItemRepository shopSalesItemRepository;

    public List<ShopSalesItem> create(Long shopId, ShopSalesItemDTO userDTO) {
        return userDTO
            .getItems()
            .stream()
            .map(item -> {
                ShopSalesItem shopSalesItem = new ShopSalesItem();
                shopSalesItem.setShopId(shopId);
                shopSalesItem.setName(item.name());
                shopSalesItem.setPrice(item.price());
                return shopSalesItemRepository.save(shopSalesItem);
            })
            .toList();
    }
}
