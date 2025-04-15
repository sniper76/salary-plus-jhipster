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
public class ShopSalaryDetailDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String date;
    private Integer salaryPrice;
    private Integer penaltyPrice;
}
