package com.shopping.dto;

import com.shopping.constant.ItemSellStatus;
import com.shopping.domain.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class ItemFormDto {

    private Long itemId;
    private String itemName;
    private Integer price;
    private String itemDetail;
    private int quantity;
    private int itemSellStatus;
    private List<MultipartFile> imageFiles;
    private MultipartFile attachFile;

    private Member seller;

}
