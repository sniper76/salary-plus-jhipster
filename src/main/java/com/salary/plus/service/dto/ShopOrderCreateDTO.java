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

    private Long orderId;
    private Long tableId;
    private List<Long> orderDetailIds;
    private List<Long> modelIds;
    private List<Long> salesItemIds;
    private List<Integer> prices;

    @Override
    public String toString() {
        return (
            "ShopOrderCreateDTO{" +
            ", orderId=" +
            orderId +
            ", tableId='" +
            tableId +
            ", orderDetailIds=" +
            orderDetailIds +
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
