package com.salary.plus.service.dto;

import com.salary.plus.domain.ShopOrder;
import com.salary.plus.domain.ShopTable;
import com.salary.plus.domain.ShopUserDailySalary;
import com.salary.plus.domain.User;
import java.io.Serializable;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO representing a user, with his authorities.
 */
@Getter
@Setter
public class ShopUserResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;
    private String firstName;
    private String lastName;
    private String modelNo;
    private boolean isCheckIn;
    private Instant createdDate;

    public ShopUserResponse() {
        // Empty constructor needed for Jackson.
    }

    public ShopUserResponse(User user, ShopUserDailySalary shopUserDailySalary) {
        this.userId = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.modelNo = user.getModelNo();
        this.isCheckIn = shopUserDailySalary == null ? false : true;
        this.createdDate = shopUserDailySalary == null ? null : shopUserDailySalary.getCreatedDate();
    }

    @Override
    public String toString() {
        return (
            "ShopUserResponse{" +
            "userId=" +
            userId +
            ", firstName='" +
            firstName +
            '\'' +
            ", lastName='" +
            lastName +
            '\'' +
            ", modelNo='" +
            modelNo +
            '\'' +
            ", isCheckIn=" +
            isCheckIn +
            ", createdDate=" +
            createdDate +
            '}'
        );
    }
}
