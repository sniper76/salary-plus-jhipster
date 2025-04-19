package com.salary.plus.repository;

import com.salary.plus.domain.ShopOrderDetail;
import com.salary.plus.domain.User;
import com.salary.plus.service.dto.ShopOrderDetailDTO;
import com.salary.plus.service.dto.ShopOrderDetailResponse;
import com.salary.plus.service.dto.ShopOrderDetailWithDiscountResponse;
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

    @Query(
        value = """
            select l.shop_id, l.date,
                    l.order_detail_price,
                    l.order_detail_discount_price,
                    jssr.shop_price as shop_refund_price,
                    jssr.model_price as model_refund_price,
                    jssr.mama_price as mama_refund_price
            from (
                select jso.shop_id,
                         jso.date,
                         sum(jsod.price)                   as order_detail_price,
                         sum(jssid.price)                  as discount_price,
                         sum(jsodd.price)                  as order_detail_discount_price,
                         sum(jsuss.price)                  as user_sales_price,
                         sum(jssi.shop_commission_price)   as shop_commission_price,
                         sum(jssi.model_commission_price)  as model_commission_price,
                         sum(jssi.mama_commission_price)   as mama_commission_price,
                         sum(jssid.shop_commission_price)  as discount_shop_commission_price,
                         sum(jssid.model_commission_price) as discount_model_commission_price,
                         sum(jssid.mama_commission_price)  as discount_mama_commission_price
                from jhi_shop_order jso
                       inner join jhi_shop_order_detail jsod on jso.id = jsod.shop_order_id and jsod.activated = true
                       inner join jhi_shop_sales_item jssi on jso.shop_id = jssi.shop_id and jsod.shop_sales_item_id = jssi.id
                       left outer join jhi_shop_sales_item_discount jssid on jssi.id = jssid.shop_sales_item_id
                       left outer join jhi_shop_order_detail_discount jsodd
                                       on jsod.id = jsodd.shop_order_detail_id and jssid.id = jsodd.shop_sales_item_discount_id
                       left outer join jhi_shop_user_sales_salary jsuss on jsod.id = jsuss.shop_order_detail_id and jsuss.activated = true
                       left outer join jhi_user ju on jsuss.user_id = ju.id
                where jso.date between :startDate and :endDate
                and jso.shop_id = :shopId
                group by jso.shop_id, jso.date
                order by jso.date
            ) l
            left outer join jhi_shop_sales_refund jssr on l.shop_id = jssr.shop_id and l.date = jssr.date
            order by l.date desc
        """,
        nativeQuery = true
    )
    List<ShopSalesDTO> findAllByShopIdAndSearchDate(Long shopId, String startDate, String endDate);

    @Query(
        value = """
            select jssi.id,
                   jssi.name_ko, jssi.name_en,
                   jsod.price as order_detail_price,
                   jsodd.price as order_detail_discount_price,
                   ju.model_no,
                   jssi.shop_commission_price,
                   jssi.model_commission_price,
                   jssi.mama_commission_price,
                   jssid.shop_commission_price as discount_shop_commission_price,
                   jssid.model_commission_price as discount_model_commission_price,
                   jssid.mama_commission_price as discount_mama_commission_price,
                   jssi.commission_target_yn, jssi.snack_yn
            from jhi_shop_order jso
            inner join jhi_shop_order_detail jsod on jso.id = jsod.shop_order_id and jsod.activated = true
            inner join jhi_shop_sales_item jssi on jso.shop_id = jssi.shop_id and jsod.shop_sales_item_id = jssi.id
            left outer join jhi_shop_sales_item_discount jssid on jssi.id = jssid.shop_sales_item_id
            left outer join jhi_shop_order_detail_discount jsodd on jsod.id = jsodd.shop_order_detail_id and jssid.id = jsodd.shop_sales_item_discount_id
            left outer join jhi_shop_user_sales_salary jsuss on jsod.id = jsuss.shop_order_detail_id and jsuss.activated = true
            left outer join jhi_user ju on jsuss.user_id = ju.id
            where jso.shop_id = :shopId
            and jso.id = :orderId
            union all
            select null, '환불', 'refund', null, null, null,
                   jssr.shop_price, jssr.model_price, jssr.mama_price, null, null, null,
                   false, false
            from jhi_shop_order jso
            left outer join jhi_shop_sales_refund jssr on jso.id = jssr.order_id and jso.shop_id = jssr.shop_id and jso.date = jssr.date
            where jso.shop_id = :shopId
              and jso.id = :orderId
            order by 1
        """,
        nativeQuery = true
    )
    List<ShopOrderDetailDTO> findAllByShopIdAndOrderId(Long shopId, Long orderId);
}
