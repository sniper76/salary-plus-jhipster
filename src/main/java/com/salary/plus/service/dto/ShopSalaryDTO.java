package com.salary.plus.service.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO representing a user, with his authorities.
 */
@Getter
@Setter
@AllArgsConstructor
public class ShopSalaryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String date;
    private Long price;
    private Long penaltyPrice;
}
