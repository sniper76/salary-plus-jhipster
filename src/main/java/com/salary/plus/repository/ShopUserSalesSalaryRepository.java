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
}
