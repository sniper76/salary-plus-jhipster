package com.salary.plus.service.dto;

import com.salary.plus.domain.ShopOrderDetailDiscount;
import com.salary.plus.domain.ShopSalesItemDiscount;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO representing a user, with his authorities.
 */
@Getter
@Setter
public class ShopOrderDiscountResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String discountNameKo;
    private String discountNameEn;
    private Integer discountPrice;

    public ShopOrderDiscountResponse() {}

    public ShopOrderDiscountResponse(ShopOrderDetailDiscount shopOrderDetailDiscount, ShopSalesItemDiscount shopSalesItemDiscount) {
        this.discountNameKo = shopSalesItemDiscount.getNameKo();
        this.discountNameEn = shopSalesItemDiscount.getNameEn();
        this.discountPrice = shopOrderDetailDiscount.getPrice();
    }
}
