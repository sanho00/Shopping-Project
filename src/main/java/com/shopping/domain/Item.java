package com.shopping.domain;

import com.shopping.constant.ItemSellStatus;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // 상품 코드

    private String name; // 상품명
    private int price; // 상품 가격

    private int count; // 상품 수량
    private int stock;

    private String text; // 상품 상세 설명

    private int isSoldOut; // 상품 판매 상태, 0: 판매중, 1: 품절

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seller_id")
    private Member seller; // 판매자 아이디

    @Builder.Default
    @OneToMany(mappedBy = "item")
    private List<CartItem> cartItems = new ArrayList<>();

    private String imgName;

    private String imgPath;


}
