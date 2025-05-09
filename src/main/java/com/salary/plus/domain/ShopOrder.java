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
@Table(name = "jhi_shop_order")
public class ShopOrder extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shop_id", nullable = false)
    private Long shopId;

    @Column(name = "shop_table_id", nullable = false)
    private Long shopTableId;

    @Column(name = "date", nullable = false)
    private String date;

    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;

    @Column(name = "discount_price", nullable = false)
    private Integer discountPrice;

    @Column(name = "refund_price", nullable = false)
    private Integer refundPrice;

    @NotNull
    @Column(name = "paid", nullable = false)
    private boolean paid = false; //손님이 계산완료 한 상태

    @NotNull
    @Column(name = "activated", nullable = false)
    private boolean activated = true;
}
