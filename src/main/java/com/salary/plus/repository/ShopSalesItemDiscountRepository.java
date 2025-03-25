package com.salary.plus.repository;

import com.salary.plus.domain.ShopSalesItemDiscount;
import com.salary.plus.domain.User;
import com.salary.plus.service.dto.ShopSalesItemDiscountResponse;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface ShopSalesItemDiscountRepository extends JpaRepository<ShopSalesItemDiscount, Long> {
    @Query(
        """
            select ssid
            from ShopSalesItemDiscount ssid
            inner join ShopSalesItem ssi on ssid.shopSalesItemId = ssi.id
            inner join Shop s on ssi.shopId = s.id
            where s.id = :shopId
        """
    )
    Page<ShopSalesItemDiscount> findAllByShopId(Long shopId, Pageable pageable);

    @Query(
        """
            select ssid
            from ShopSalesItemDiscount ssid
            inner join ShopSalesItem ssi on ssid.shopSalesItemId = ssi.id
            inner join Shop s on ssi.shopId = s.id
            where s.id = :shopId
            and s.activated = :activated
        """
    )
    List<ShopSalesItemDiscount> findAllByShopIdAndActivated(Long shopId, boolean activated);

    @Query(
        """
            select new com.salary.plus.service.dto.ShopSalesItemDiscountResponse(sod, ssid)
            from ShopSalesItemDiscount ssid
            inner join ShopSalesItem ssi on ssid.shopSalesItemId = ssi.id
            inner join Shop s on ssi.shopId = s.id
            inner join ShopOrderDetail sod on ssi.id = sod.shopSalesItemId
            inner join ShopOrder so on sod.shopOrderId = so.id and s.id = so.shopId
            where s.id = :shopId
            and so.id = :orderId
            and s.activated = :activated
            and ssi.activated = :activated
        """
    )
    List<ShopSalesItemDiscountResponse> findAllByShopIdAndOrderIdAndActivated(Long shopId, Long orderId, boolean activated);

    Optional<ShopSalesItemDiscount> findByIdAndActivated(long id, boolean activated);
}
