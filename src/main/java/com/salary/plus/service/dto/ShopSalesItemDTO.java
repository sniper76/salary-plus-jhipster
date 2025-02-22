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
public class ShopSalesItemDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private List<SalesItem> items;

    public record SalesItem(String name, Integer price) {}
}
