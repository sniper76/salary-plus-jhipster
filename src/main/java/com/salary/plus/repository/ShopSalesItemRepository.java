package com.salary.plus.repository;

import com.salary.plus.domain.ShopSalesItem;
import com.salary.plus.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface ShopSalesItemRepository extends JpaRepository<ShopSalesItem, Long> {
    List<ShopSalesItem> findAllByShopIdAndActivated(Long shopId, boolean activated);

    Page<ShopSalesItem> findAllByShopId(Long shopId, Pageable pageable);

    Optional<ShopSalesItem> findByIdAndShopIdAndActivated(long salesItemId, Long shopId, boolean activated);
}
