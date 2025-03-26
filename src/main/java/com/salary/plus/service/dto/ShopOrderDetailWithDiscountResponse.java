package com.salary.plus.service.dto;

import com.salary.plus.domain.ShopOrder;
import com.salary.plus.domain.ShopOrderDetail;
import com.salary.plus.domain.ShopSalesItem;
import com.salary.plus.domain.ShopSalesItemDiscount;
import com.salary.plus.domain.ShopUserSalesSalary;
import java.io.Serializable;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO representing a user, with his authorities.
 */
@Getter
@Setter
public class ShopOrderDetailWithDiscountResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id; //shopSalesItemDiscountId;
    private Long shopSalesItemId;
    private String nameKo;
    private String nameEn;
    private Integer price;
    private String createdBy;
    private Instant createdDate;
    private String lastModifiedBy;
    private Instant lastModifiedDate;

    public ShopOrderDetailWithDiscountResponse() {
        // Empty constructor needed for Jackson.
    }

    public ShopOrderDetailWithDiscountResponse(ShopSalesItem shopSalesItem, ShopSalesItemDiscount shopSalesItemDiscount) {
        this.id = shopSalesItemDiscount.getId();
        this.shopSalesItemId = shopSalesItem.getId();
        this.nameKo = shopSalesItem.getNameKo();
        this.nameEn = shopSalesItem.getNameEn();
        this.price = shopSalesItemDiscount.getPrice();
        this.createdBy = shopSalesItemDiscount.getCreatedBy();
        this.createdDate = shopSalesItemDiscount.getCreatedDate();
        this.lastModifiedBy = shopSalesItemDiscount.getLastModifiedBy();
        this.lastModifiedDate = shopSalesItemDiscount.getLastModifiedDate();
    }
}
