package com.salary.plus.service.dto;

import com.salary.plus.domain.ShopBaseSalary;
import com.salary.plus.domain.ShopOrder;
import com.salary.plus.domain.ShopOrderDetail;
import com.salary.plus.domain.ShopSalesItem;
import com.salary.plus.domain.ShopUserSalesSalary;
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

    public ShopUserSalaryBaseMappingResponse(User user, ShopBaseSalary shopBaseSalary) {
        this.id = shopBaseSalary.getId();
        this.login = user.getLogin();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.modelNo = user.getModelNo();
        this.price = shopBaseSalary.getPrice();
        this.createdBy = shopBaseSalary.getCreatedBy();
        this.createdDate = shopBaseSalary.getCreatedDate();
        this.lastModifiedBy = shopBaseSalary.getLastModifiedBy();
        this.lastModifiedDate = shopBaseSalary.getLastModifiedDate();
    }
}
