package com.salary.plus.service.dto;

import com.salary.plus.config.Constants;
import com.salary.plus.domain.Shop;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
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

    @NotBlank
    @Size(min = 1, max = 8)
    @Pattern(
        regexp = Constants.WORK_START_TIME_REGEX,
        message = "유효하지 않은 시간 형식입니다. 올바른 형식은 HH:mm:ss 입니다 (예: 09:00:00 또는 23:59:59)."
    )
    private String workStartTime;

    private boolean activated = false;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    public AdminShopDTO() {
        // Empty constructor needed for Jackson.
    }

    public AdminShopDTO(Shop shop) {
        this.id = shop.getId();
        this.nameKo = shop.getNameKo();
        this.nameEn = shop.getNameEn();
        this.type = shop.getType().name();
        this.workStartTime = shop.getWorkStartTime();
        this.activated = shop.isActivated();
        this.createdBy = shop.getCreatedBy();
        this.createdDate = shop.getCreatedDate();
        this.lastModifiedBy = shop.getLastModifiedBy();
        this.lastModifiedDate = shop.getLastModifiedDate();
    }

    @Override
    public String toString() {
        return "AdminShopDTO{" + "nameKo='" + nameKo + '\'' + ", nameEn='" + nameEn + '\'' + ", type='" + type + '\'' + "}";
    }
}
