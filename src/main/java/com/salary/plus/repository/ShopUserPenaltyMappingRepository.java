package com.salary.plus.repository;

import com.salary.plus.domain.ShopUserPenaltyMapping;
import com.salary.plus.domain.User;
import com.salary.plus.service.dto.ShopOrderDetailDTO;
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

    @Query(
        value = """
            select null as id, '벌금' as name_ko, 'penalty' as name_en,
                cast(sum(jssr.price) as integer) as order_detail_price,
                0 as order_detail_discount_price, jssr.model_no,
                0 as shop_commission_price, 0 as model_commission_price, 0 as mama_commission_price,
                0 as discount_shop_commission_price, 0 as discount_model_commission_price, 0 as discount_mama_commission_price,
                false as commission_target_yn, false as snack_yn
            from (
                select sp.shop_id, to_char(supm.created_date - interval '1 day', 'yyyy-MM-dd') as date,
                   supm.price, supm.user_id, ju.model_no
                from jhi_shop_penalty sp
                     inner join jhi_shop_user_penalty_mapping supm on sp.id = supm.shop_penalty_id
                     inner join jhi_user ju on supm.user_id = ju.id
                where sp.shop_id = :shopId
                and supm.date = :date
                and supm.user_id in :userIds
                and sp.type = 'BAR_SHARE'
                union all
                select sp.shop_id, supm.date,
                   supm.price, supm.user_id, ju.model_no
                from jhi_shop_penalty sp
                     inner join jhi_shop_user_penalty_mapping supm on sp.id = supm.shop_penalty_id
                     inner join jhi_user ju on supm.user_id = ju.id
                where sp.shop_id = :shopId
                and supm.date = :date
                and supm.user_id in :userIds
                and sp.type <> 'BAR_SHARE'
            ) jssr
            group by jssr.model_no
        """,
        nativeQuery = true
    )
    List<ShopOrderDetailDTO> findAllByShopIdAndDate(Long shopId, String date, List<Long> userIds);
}
