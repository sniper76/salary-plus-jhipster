package com.salary.plus.service.dto;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO representing a user, with his authorities.
 */
@Getter
@Setter
public class ShopOrderRefundCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String login;
    private Long shopId;
    private String date;
    private Long orderId;
    private Integer shopPrice;
    private Integer modelPrice;
    private Integer mamaPrice;
}
