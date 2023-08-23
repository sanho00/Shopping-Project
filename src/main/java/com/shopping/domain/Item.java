package com.shopping.domain;

import com.shopping.constant.ItemSellStatus;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "item")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id; // 상품 코드

    @Column(nullable = false, length = 50)
    private String itemName; // 상품명

    @Column(nullable = false, name = "price")
    private int price; // 상품 가격

    @Column(nullable = false)
    private int quantity; // 상품 수량

    @Lob
    @Column(nullable = false)
    private String itemDetail; // 상품 상세 설명

    private int itemSellStatus; // 상품 판매 상태, 0: 판매중, 1: 품절

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seller_id")
    private Member seller; // 판매자 아이디

    @OneToMany(mappedBy = "item")
    private List<CartItem> cartItems = new ArrayList<>();

    private String imgName;

    private String imgPath;


}
