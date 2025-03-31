package com.salary.plus.service.dto;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO representing a user, with his authorities.
 */
@Getter
@Setter
public class ModelMappingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long shopId;
    private Long userId;
    private List<Long> modelUserIds;

    @Override
    public String toString() {
        return "ModelMappingDTO{" + "shopId=" + shopId + ", userId=" + userId + ", modelUserIds=" + modelUserIds + '}';
    }
}
