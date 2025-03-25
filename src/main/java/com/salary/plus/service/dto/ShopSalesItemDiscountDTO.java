package com.salary.plus.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO representing a user, with his authorities.
 */
@Getter
@Setter
public class ShopSalesItemDiscountDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long shopId;
    private Long salesItemId;
    private String login;

    @NotBlank
    @Size(min = 1, max = 50)
    private String nameKo;

    @NotBlank
    @Size(min = 1, max = 50)
    private String nameEn;

    @NotBlank
    @Size(min = 1, max = 50)
    private String type;

    private boolean activated = false;

    @PositiveOrZero
    private Integer price;

    @PositiveOrZero
    private Integer shopCommissionPrice;

    @PositiveOrZero
    private Integer mamaCommissionPrice;

    @PositiveOrZero
    private Integer modelCommissionPrice;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;
}
