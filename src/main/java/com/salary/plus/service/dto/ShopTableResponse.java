package com.salary.plus.service.dto;

import com.salary.plus.domain.ShopTable;
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
public class ShopTableResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String no;
    private String createdBy;
    private Instant createdDate;
    private String lastModifiedBy;
    private Instant lastModifiedDate;

    public ShopTableResponse() {
        // Empty constructor needed for Jackson.
    }

    public ShopTableResponse(ShopTable shopTable) {
        this.id = shopTable.getId();
        this.no = shopTable.getNo();
        this.createdBy = shopTable.getCreatedBy();
        this.createdDate = shopTable.getCreatedDate();
        this.lastModifiedBy = shopTable.getLastModifiedBy();
        this.lastModifiedDate = shopTable.getLastModifiedDate();
    }
}
