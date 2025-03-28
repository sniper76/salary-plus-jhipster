package com.salary.plus.service.dto;

import com.salary.plus.domain.ShopOrderDetailDiscount;
import com.salary.plus.domain.ShopSalesItemDiscount;
import com.salary.plus.domain.ShopSalesRefund;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO representing a user, with his authorities.
 */
@Getter
@Setter
public class ShopOrderRefundResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer shopPrice;
    private Integer modelPrice;
    private Integer mamaPrice;

    public ShopOrderRefundResponse() {}

    public ShopOrderRefundResponse(ShopSalesRefund shopSalesRefund) {
        this.shopPrice = shopSalesRefund.getShopPrice();
        this.modelPrice = shopSalesRefund.getModelPrice();
        this.mamaPrice = shopSalesRefund.getMamaPrice();
    }
}
