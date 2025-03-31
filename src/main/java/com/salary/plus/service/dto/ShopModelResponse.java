package com.salary.plus.service.dto;

import com.salary.plus.domain.ShopUserToUserMapping;
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
public class ShopModelResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String login;
    private String firstName;
    private String lastName;
    private String modelNo;
    private Long targetUserId;
    private String createdBy;
    private Instant createdDate;
    private String lastModifiedBy;
    private Instant lastModifiedDate;

    public ShopModelResponse() {
        // Empty constructor needed for Jackson.
    }

    public ShopModelResponse(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.modelNo = user.getModelNo();
        this.createdBy = user.getCreatedBy();
        this.createdDate = user.getCreatedDate();
        this.lastModifiedBy = user.getLastModifiedBy();
        this.lastModifiedDate = user.getLastModifiedDate();
    }

    public ShopModelResponse(User user, ShopUserToUserMapping mapping) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.modelNo = user.getModelNo();
        this.createdBy = user.getCreatedBy();
        this.createdDate = user.getCreatedDate();
        this.lastModifiedBy = user.getLastModifiedBy();
        this.lastModifiedDate = user.getLastModifiedDate();
        this.targetUserId = getTargetUserId(mapping);
    }

    private Long getTargetUserId(ShopUserToUserMapping mapping) {
        if (mapping == null) {
            return null;
        }
        return mapping.getTargetUserId();
    }
}
