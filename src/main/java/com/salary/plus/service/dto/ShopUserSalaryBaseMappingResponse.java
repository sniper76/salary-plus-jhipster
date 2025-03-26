package com.salary.plus.service.dto;

import com.salary.plus.domain.ShopBaseSalary;
import com.salary.plus.domain.ShopUserBaseSalaryMapping;
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
public class ShopUserSalaryBaseMappingResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id; //shopUserBaseSalaryMappingId;
    private Long userId;
    private String login;
    private String firstName;
    private String lastName;
    private String modelNo;
    private Integer price;
    private String createdBy;
    private Instant createdDate;
    private String lastModifiedBy;
    private Instant lastModifiedDate;

    public ShopUserSalaryBaseMappingResponse() {
        // Empty constructor needed for Jackson.
    }

    public ShopUserSalaryBaseMappingResponse(User user) {
        this.userId = user.getId();
        this.login = user.getLogin();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.modelNo = user.getModelNo();
        this.createdBy = user.getCreatedBy();
        this.createdDate = user.getCreatedDate();
        this.lastModifiedBy = user.getLastModifiedBy();
        this.lastModifiedDate = user.getLastModifiedDate();
    }

    public ShopUserSalaryBaseMappingResponse(ShopBaseSalary shopBaseSalary, ShopUserBaseSalaryMapping shopUserBaseSalaryMapping) {
        this.id = shopUserBaseSalaryMapping.getId();
        this.price = getPrice(shopBaseSalary, shopUserBaseSalaryMapping);
    }

    private Integer getPrice(ShopBaseSalary shopBaseSalary, ShopUserBaseSalaryMapping shopUserBaseSalaryMapping) {
        if (shopUserBaseSalaryMapping == null) {
            return null;
        }
        return shopBaseSalary.getPrice();
    }
}
