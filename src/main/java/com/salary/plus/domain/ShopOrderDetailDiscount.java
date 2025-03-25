package com.salary.plus.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * A user.
 */
@Entity
@Getter
@Setter
@Table(name = "jhi_shop_order_detail_discount")
public class ShopOrderDetailDiscount extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shop_order_detail_id", nullable = false)
    private Long shopOrderDetailId;

    @Column(name = "shop_sales_item_discount_id", nullable = false)
    private Long shopSalesItemDiscountId;

    @Column(name = "price", nullable = false)
    private Integer price;

    @NotNull
    @Column(name = "activated", nullable = false)
    private boolean activated = true;

    @Override
    public void delete(String login) {
        super.delete(login);
        this.activated = false;
    }
}
