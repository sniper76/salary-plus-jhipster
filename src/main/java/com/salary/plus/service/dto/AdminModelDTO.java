package com.salary.plus.service.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO representing a user, with his authorities.
 */
@Getter
@Setter
public class AdminModelDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long shopId;
    private String csvData;
}
