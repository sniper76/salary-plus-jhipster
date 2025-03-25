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
public class ShopSalesItemDiscountResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id; //shopSalesItemDiscountId;
    private Long orderDetailId;
    private String nameKo;
    private String nameEn;
    private Integer price;
    private String createdBy;
    private Instant createdDate;
    private String lastModifiedBy;
    private Instant lastModifiedDate;

    public ShopSalesItemDiscountResponse() {
        // Empty constructor needed for Jackson.
    }

    public ShopSalesItemDiscountResponse(ShopOrderDetail shopOrderDetail, ShopSalesItemDiscount shopSalesItemDiscount) {
        this.id = shopSalesItemDiscount.getId();
        this.orderDetailId = shopOrderDetail.getId();
        this.nameKo = shopSalesItemDiscount.getNameKo();
        this.nameEn = shopSalesItemDiscount.getNameEn();
        this.price = shopOrderDetail.getPrice();
        this.createdBy = shopOrderDetail.getCreatedBy();
        this.createdDate = shopOrderDetail.getCreatedDate();
        this.lastModifiedBy = shopOrderDetail.getLastModifiedBy();
        this.lastModifiedDate = shopOrderDetail.getLastModifiedDate();
    }

    @Override
    public String toString() {
        return (
            "ShopOrderDetailResponse{" +
            "id=" +
            id +
            ", orderDetailId=" +
            orderDetailId +
            ", nameKo='" +
            nameKo +
            '\'' +
            ", nameEn='" +
            nameEn +
            '\'' +
            ", price=" +
            price +
            ", createdBy='" +
            createdBy +
            '\'' +
            ", createdDate=" +
            createdDate +
            ", lastModifiedBy='" +
            lastModifiedBy +
            '\'' +
            ", lastModifiedDate=" +
            lastModifiedDate +
            '}'
        );
    }
}
