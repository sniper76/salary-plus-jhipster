package com.salary.plus.repository;

import com.salary.plus.domain.ShopUserSalesSalary;
import com.salary.plus.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface ShopUserSalesSalaryRepository extends JpaRepository<ShopUserSalesSalary, Long> {
    Optional<ShopUserSalesSalary> findByShopOrderDetailId(Long shopOrderDetailId);

    @Query(
        """
            select suss
            from ShopOrder so
            inner join ShopOrderDetail sod on so.id = sod.shopOrderId
            inner join ShopSalesItem ssi on sod.shopSalesItemId = ssi.id
            inner join ShopUserSalesSalary suss on sod.id = suss.shopOrderDetailId
            where so.shopId = :shopId
            and so.date = :date
            and suss.userId = :userId
            and ssi.commissionTargetYn = true
        """
    )
    List<ShopUserSalesSalary> findAllByShopIdAndDateAndUserId(Long shopId, String date, Long userId);

    @Query(
        value = """
            select jsuss.user_id
            from jhi_shop_order jso
            inner join jhi_shop_order_detail jsod on jso.id = jsod.shop_order_id and jsod.activated = true
            inner join jhi_shop_sales_item jssi on jso.shop_id = jssi.shop_id and jsod.shop_sales_item_id = jssi.id
            inner join jhi_shop_user_sales_salary jsuss on jsod.id = jsuss.shop_order_detail_id and jsuss.activated = true
            where jso.shop_id = :shopId
            and jso.date = :date
            and jso.id = :orderId
        """,
        nativeQuery = true
    )
    List<Long> findByShopIdAndDateAndOrderId(Long shopId, String date, Long orderId);
}
