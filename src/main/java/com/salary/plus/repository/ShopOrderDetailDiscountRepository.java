package com.salary.plus.repository;

import com.salary.plus.domain.ShopOrderDetailDiscount;
import com.salary.plus.domain.User;
import com.salary.plus.service.dto.ShopOrderDiscountResponse;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface ShopOrderDetailDiscountRepository extends JpaRepository<ShopOrderDetailDiscount, Long> {
    Optional<ShopOrderDetailDiscount> findByShopOrderDetailIdAndShopSalesItemDiscountId(Long orderDetailId, Long salesItemDiscountId);

    @Query(
        """
            select new com.salary.plus.service.dto.ShopOrderDiscountResponse(sodd, ssid)
            from ShopOrderDetailDiscount sodd
            inner join ShopOrderDetail sod on sodd.shopOrderDetailId = sod.id
            inner join ShopSalesItemDiscount ssid on sod.shopSalesItemId = ssid.shopSalesItemId
            where sod.shopOrderId = :orderId
        """
    )
    List<ShopOrderDiscountResponse> findAllByOrderId(Long orderId);
}
