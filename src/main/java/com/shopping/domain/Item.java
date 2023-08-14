package com.shopping.domain;

import com.shopping.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "item")
@Getter
@Setter
@ToString
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "itemId")
    private Long itemId; // 상품 코드

    @Column(nullable = false, length = 50)
    private String itemName; // 상품명

    @Column(nullable = false, name = "price")
    private int price; // 상품 가격

    @Lob
    @Column(nullable = false)
    private String itemDetail; // 상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itmeSellStatus; // 상품 판매 상태

}
