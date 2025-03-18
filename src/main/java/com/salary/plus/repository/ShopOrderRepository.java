package com.salary.plus.repository;

import com.salary.plus.domain.ShopOrder;
import com.salary.plus.domain.User;
import com.salary.plus.service.dto.ShopOrderDetailResponse;
import com.salary.plus.service.dto.ShopOrderResponse;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface ShopOrderRepository extends JpaRepository<ShopOrder, Long> {
    @Query(
        """
            select new com.salary.plus.service.dto.ShopOrderResponse(so, st)
            from ShopOrder so inner join ShopTable st on so.shopTableId = st.id
            where so.shopId = :shopId
            and so.date = :date
            and so.activated = :activated
        """
    )
    List<ShopOrderResponse> findAllByShopIdAndDateAndActivated(Long shopId, String date, boolean activated);

    Optional<ShopOrder> findByIdAndShopIdAndDate(Long orderId, Long shopId, String date);

    @Query(
        """
            select new com.salary.plus.service.dto.ShopOrderDetailResponse(so, sod, ssi, suss)
            from ShopOrder so
            inner join ShopOrderDetail sod on so.id = sod.shopOrderId
            left outer join ShopSalesItem ssi on sod.shopSalesItemId = ssi.id and ssi.activated = :activated
            left outer join ShopUserSalesSalary suss on sod.id = suss.shopOrderDetailId and suss.activated = :activated
            where so.shopId = :shopId
            and so.date = :date
            and so.id = :orderId
            and so.activated = :activated
            and sod.activated = :activated
            order by sod.id
        """
    )
    List<ShopOrderDetailResponse> findAllByIdAndShopIdAndDate(Long orderId, Long shopId, String date, boolean activated);
}
