package com.salary.plus.service.dto;

import com.salary.plus.domain.Authority;
import com.salary.plus.domain.Shop;
import com.salary.plus.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO representing a user, with his authorities.
 */
@Getter
@Setter
public class AdminShopDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank
    @Size(min = 1, max = 50)
    private String nameKo;

    @NotBlank
    @Size(min = 1, max = 50)
    private String nameEn;

    @NotBlank
    @Size(min = 1, max = 50)
    private String type;

    private boolean activated = false;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private List<AdminUserDTO> users;

    public AdminShopDTO() {
        // Empty constructor needed for Jackson.
    }

    public AdminShopDTO(Shop shop) {
        this.id = shop.getId();
        this.nameKo = shop.getNameKo();
        this.nameEn = shop.getNameEn();
        this.type = shop.getType().name();
        this.activated = shop.isActivated();
        this.createdBy = shop.getCreatedBy();
        this.createdDate = shop.getCreatedDate();
        this.lastModifiedBy = shop.getLastModifiedBy();
        this.lastModifiedDate = shop.getLastModifiedDate();
    }
}
