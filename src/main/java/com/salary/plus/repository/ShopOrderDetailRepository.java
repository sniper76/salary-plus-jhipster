package com.salary.plus.repository;

import com.salary.plus.domain.ShopOrderDetail;
import com.salary.plus.domain.User;
import com.salary.plus.service.dto.ShopOrderDetailWithDiscountResponse;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface ShopOrderDetailRepository extends JpaRepository<ShopOrderDetail, Long> {
    Optional<ShopOrderDetail> findByIdAndShopOrderId(Long orderDetailId, Long orderId);

    @Query(
        """
            select new com.salary.plus.service.dto.ShopOrderDetailWithDiscountResponse(ssi, ssid)
            from ShopOrder so
            inner join ShopOrderDetail sod on so.id = sod.shopOrderId
            inner join ShopSalesItem ssi on sod.shopSalesItemId = ssi.id
            inner join ShopSalesItemDiscount ssid on ssi.id = ssid.shopSalesItemId
            where so.shopId = :shopId
            and so.date = :date
            and so.id = :orderId
        """
    )
    List<ShopOrderDetailWithDiscountResponse> findAllOrderDetailWithDiscounts(Long shopId, String date, Long orderId);
}
