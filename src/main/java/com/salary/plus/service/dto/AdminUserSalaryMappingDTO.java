package com.salary.plus.service.dto;

import jakarta.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO representing a user, with his authorities.
 */
@Getter
@Setter
public class AdminUserSalaryMappingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long shopId;
    private Long userId;

    @PositiveOrZero
    private Long shopBaseSalaryId;

    @Override
    public String toString() {
        return "AdminUserSalaryMappingDTO{" + "shopId=" + shopId + ", userId=" + userId + ", shopBaseSalaryId=" + shopBaseSalaryId + '}';
    }
}
