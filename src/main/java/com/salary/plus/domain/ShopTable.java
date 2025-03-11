package com.salary.plus.domain;

import com.salary.plus.enums.PenaltyType;
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
@Table(name = "jhi_shop_table")
public class ShopTable extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shop_Id", nullable = false)
    private Long shopId;

    @Column(name = "no", nullable = false)
    private String no;

    @NotNull
    @Column(name = "activated", nullable = false)
    private boolean activated = true;

    @Override
    public String toString() {
        return ("ShopTable{" + "id=" + id + ", shopId=" + shopId + ", no='" + no + '\'' + ", activated=" + activated + '}');
    }
}
