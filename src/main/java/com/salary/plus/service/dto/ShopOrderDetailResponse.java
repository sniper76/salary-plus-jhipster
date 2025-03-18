package com.salary.plus.service.dto;

import com.salary.plus.domain.ShopOrder;
import com.salary.plus.domain.ShopOrderDetail;
import com.salary.plus.domain.ShopSalesItem;
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
public class ShopOrderDetailResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id; //salesItemId;
    private Long orderDetailId;
    private Long modelId;
    private String nameKo;
    private String nameEn;
    private boolean isCommissionTarget;
    private boolean isSnack;
    private Integer price;
    private String createdBy;
    private Instant createdDate;
    private String lastModifiedBy;
    private Instant lastModifiedDate;

    public ShopOrderDetailResponse() {
        // Empty constructor needed for Jackson.
    }

    public ShopOrderDetailResponse(
        ShopOrder shopOrder,
        ShopOrderDetail shopOrderDetail,
        ShopSalesItem shopSalesItem,
        ShopUserSalesSalary shopUserSalesSalary
    ) {
        this.id = shopSalesItem.getId();
        this.orderDetailId = shopOrderDetail.getId();
        this.modelId = getModelId(shopUserSalesSalary);
        this.nameKo = shopSalesItem.getNameKo();
        this.nameEn = shopSalesItem.getNameEn();
        this.price = shopOrderDetail.getPrice();
        this.isCommissionTarget = shopSalesItem.isCommissionTarget();
        this.isSnack = shopSalesItem.isSnack();
        this.createdBy = shopOrderDetail.getCreatedBy();
        this.createdDate = shopOrderDetail.getCreatedDate();
        this.lastModifiedBy = shopOrderDetail.getLastModifiedBy();
        this.lastModifiedDate = shopOrderDetail.getLastModifiedDate();
    }

    private Long getModelId(ShopUserSalesSalary shopUserSalesSalary) {
        if (shopUserSalesSalary == null) {
            return null;
        }
        return shopUserSalesSalary.getUserId();
    }

    @Override
    public String toString() {
        return (
            "ShopOrderDetailResponse{" +
            "id=" +
            id +
            ", orderDetailId=" +
            orderDetailId +
            ", modelId=" +
            modelId +
            ", nameKo='" +
            nameKo +
            '\'' +
            ", nameEn='" +
            nameEn +
            '\'' +
            ", isCommissionTarget=" +
            isCommissionTarget +
            ", isSnack=" +
            isSnack +
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
