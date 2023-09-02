package com.shopping.domain;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class SaleItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sale_id")
    private Sale sale;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seller_id")
    private Member seller; // 판매자

    private int itemId; // 주문 상품 번호
    private String itemName; // 주문 상품 이름
    private int itemPrice; // 주문 상품 가격
    private int itemCount; // 주문 상품 수량
    private int itemTotalPrice; // 가격 * 수량

    @OneToOne(mappedBy = "saleItem")
    private OrderItem orderItem; // 판매 상품에 매핑되는 주문 상품

    private int isCancle; // 판매 취소 여부 ( 0 - 판매완료 / 1 - 판매취소 )

    // 장바구니 전체 주문
    public static SaleItem createSaleItem(int itemId, Sale sale, Member seller, CartItem cartItem) {
        SaleItem saleItem = new SaleItem();

        saleItem.setItemId(itemId);
        saleItem.setSale(sale);
        saleItem.setSeller(seller);
        saleItem.setItemName(cartItem.getItem().getName());
        saleItem.setItemPrice(cartItem.getItem().getPrice());
        saleItem.setItemCount(cartItem.getCount());
        saleItem.setItemTotalPrice(cartItem.getItem().getPrice() * cartItem.getCount());

        return saleItem;
    }

    // 상품 개별 주문
    public static SaleItem createSaleItem(int itemId, Sale sale, Member seller, Item item, int count) {
        SaleItem saleItem = new SaleItem();

        saleItem.setItemId(itemId);
        saleItem.setSale(sale);
        saleItem.setSeller(seller);
        saleItem.setItemName(item.getName());
        saleItem.setItemPrice(item.getPrice());
        saleItem.setItemCount(count);
        saleItem.setItemTotalPrice(item.getPrice() * count);

        return saleItem;
    }
}
