package com.salary.plus.repository;

import com.salary.plus.domain.ShopUserPenaltyMapping;
import com.salary.plus.domain.User;
import com.salary.plus.service.dto.ShopSalesDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface ShopUserPenaltyMappingRepository extends JpaRepository<ShopUserPenaltyMapping, Long> {
    Optional<ShopUserPenaltyMapping> findByShopPenaltyIdAndUserIdAndDate(Long shopPenaltyId, Long userId, String date);

    @Query(
        value = """
            select sp.shop_id, supm.date,
                sum(supm.price) as order_detail_price,
                0 as order_detail_discount_price,
                0 as shop_refund_price,
                0 as model_refund_price,
                0 as mama_refund_price
            from jhi_shop_penalty sp
            inner join jhi_shop_user_penalty_mapping supm on sp.id = supm.shop_penalty_id
            where sp.shop_id = :shopId
            and supm.date between :startDate and :endDate
            group by sp.shop_id, supm.date
            order by supm.date
        """,
        nativeQuery = true
    )
    List<ShopSalesDTO> findAllShopIdAndSearchDate(Long shopId, String startDate, String endDate);
}
