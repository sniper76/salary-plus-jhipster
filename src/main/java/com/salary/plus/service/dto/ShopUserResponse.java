package com.salary.plus.service.dto;

import com.salary.plus.domain.ShopUserDailySalary;
import com.salary.plus.domain.ShopUserPenaltyMapping;
import com.salary.plus.domain.User;
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
public class ShopUserResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;
    private String firstName;
    private String lastName;
    private String modelNo;
    private boolean isCheckIn;
    private Instant createdDate;
    private boolean isAbsence;
}
