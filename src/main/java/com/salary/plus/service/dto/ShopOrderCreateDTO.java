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
public class ShopOrderCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long shopId;
    private Long orderId;
    private String date;
    private Long tableId;
    private List<Long> modelIds;
    private List<Long> salesItemIds;
    private List<Integer> prices;

    @Override
    public String toString() {
        return (
            "ShopOrderCreateDTO{" +
            "shopId=" +
            shopId +
            ", orderId=" +
            orderId +
            ", date='" +
            date +
            '\'' +
            ", tableNo='" +
            tableId +
            '\'' +
            ", modelIds=" +
            modelIds +
            ", salesItemIds=" +
            salesItemIds +
            ", prices=" +
            prices +
            '}'
        );
    }
}
