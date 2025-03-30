package com.salary.plus.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO representing a user, with his authorities.
 */
@Getter
@Setter
@AllArgsConstructor
public class ShopSalesDTO {

    private Long shopId;
    private Long orderId;
    private String date;
    private Long orderDetailPrice;
    private Long orderDetailDiscountPrice;
    private Integer shopRefundPrice;
    private Integer mamaRefundPrice;
    private Integer modeRefundPrice;
}
