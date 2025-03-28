package com.salary.plus.domain;

import com.salary.plus.enums.BonusType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "jhi_shop_sales_refund")
public class ShopSalesRefund extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shop_id", nullable = false)
    private Long shopId;

    @Column(name = "date", nullable = false)
    private String date;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "shop_price", nullable = false)
    private Integer shopPrice;

    @Column(name = "model_price", nullable = false)
    private Integer modelPrice;

    @Column(name = "mama_price", nullable = false)
    private Integer mamaPrice;

    @NotNull
    @Column(name = "activated", nullable = false)
    private boolean activated = true;
}
