package com.salary.plus.service.dto;

import com.salary.plus.domain.ShopOrder;
import com.salary.plus.domain.ShopOrderDetail;
import com.salary.plus.domain.ShopSalesItem;
import com.salary.plus.domain.ShopUserSalesSalary;
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
public class ShopOrderDetailDTO {

    private Long id; //salesItemId;
    private String nameKo;
    private String nameEn;
    private Integer orderDetailPrice;
    private Integer orderDetailDiscountPrice;
    private String modelNo;
    private Integer shopCommissionPrice;
    private Integer modelCommissionPrice;
    private Integer mamaCommissionPrice;
    private Integer discountShopCommissionPrice;
    private Integer discountModelCommissionPrice;
    private Integer discountMamaCommissionPrice;
    private boolean commissionTargetYn;
    private boolean snackYn;

    @Override
    public String toString() {
        return (
            "ShopOrderDetailDTO{" +
            "id=" +
            id +
            ", nameKo='" +
            nameKo +
            '\'' +
            ", nameEn='" +
            nameEn +
            '\'' +
            ", orderDetailPrice=" +
            orderDetailPrice +
            ", orderDetailDiscountPrice=" +
            orderDetailDiscountPrice +
            ", modelNo='" +
            modelNo +
            '\'' +
            ", shopCommissionPrice=" +
            shopCommissionPrice +
            ", modelCommissionPrice=" +
            modelCommissionPrice +
            ", mamaCommissionPrice=" +
            mamaCommissionPrice +
            ", discountShopCommissionPrice=" +
            discountShopCommissionPrice +
            ", discountModelCommissionPrice=" +
            discountModelCommissionPrice +
            ", discountMamaCommissionPrice=" +
            discountMamaCommissionPrice +
            ", commissionTargetYn=" +
            commissionTargetYn +
            ", snackYn=" +
            snackYn +
            '}'
        );
    }
}
