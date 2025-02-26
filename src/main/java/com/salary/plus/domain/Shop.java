package com.salary.plus.domain;

import com.salary.plus.enums.ShopType;
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
@Table(name = "jhi_shop")
public class Shop extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_ko", nullable = false)
    private String nameKo;

    @Column(name = "name_en", nullable = false)
    private String nameEn;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ShopType type;

    @NotNull
    @Column(name = "activated", nullable = false)
    private boolean activated = true;

    @Override
    public String toString() {
        return (
            "Shop{" +
            "id=" +
            id +
            ", nameKo='" +
            nameKo +
            '\'' +
            ", nameEn='" +
            nameEn +
            '\'' +
            ", type=" +
            type +
            ", activated=" +
            activated +
            '}'
        );
    }
}
