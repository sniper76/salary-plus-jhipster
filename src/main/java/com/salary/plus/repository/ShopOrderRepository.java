package com.salary.plus.repository;

import com.salary.plus.domain.ShopOrder;
import com.salary.plus.domain.User;
import com.salary.plus.service.dto.ShopOrderResponse;
import java.util.List;
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
        """
    )
    List<ShopOrderResponse> findAllByShopIdAndDateAndActivated(Long shopId, String date, boolean activated);
}
