package com.shopping.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDto {
    private Long itemId;
    private String itemName;
    private Integer price;
    private String itemDetail;
    private String sellStatus;
}
