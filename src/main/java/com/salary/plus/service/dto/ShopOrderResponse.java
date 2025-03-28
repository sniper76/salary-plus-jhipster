package com.salary.plus.service.dto;

import com.salary.plus.domain.ShopOrder;
import com.salary.plus.domain.ShopTable;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO representing a user, with his authorities.
 */
@Getter
@Setter
public class ShopOrderResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long orderId;
    private Long shopTableId;
    private String tableNo;
    private Integer totalPrice;
    private boolean paid;
    private String createdBy;
    private Instant createdDate;
    private String lastModifiedBy;
    private Instant lastModifiedDate;
    private List<ShopOrderDiscountResponse> discountResponseList;
    private ShopOrderRefundResponse refundResponse;

    public ShopOrderResponse() {
        // Empty constructor needed for Jackson.
    }

    public ShopOrderResponse(ShopOrder shopOrder, ShopTable shopTable) {
        this.orderId = shopOrder.getId();
        this.shopTableId = shopOrder.getShopTableId();
        this.tableNo = shopTable.getNo();
        this.totalPrice = shopOrder.getTotalPrice();
        this.paid = shopOrder.isPaid();
        this.createdBy = shopOrder.getCreatedBy();
        this.createdDate = shopOrder.getCreatedDate();
        this.lastModifiedBy = shopOrder.getLastModifiedBy();
        this.lastModifiedDate = shopOrder.getLastModifiedDate();
    }

    @Override
    public String toString() {
        return (
            "ShopOrderResponse{" +
            "orderId=" +
            orderId +
            ", shopTableId=" +
            shopTableId +
            ", tableNo='" +
            tableNo +
            '\'' +
            ", totalPrice=" +
            totalPrice +
            ", paid=" +
            paid +
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
