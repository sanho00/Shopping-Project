package com.shopping.domain;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member; // 구매자

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Order order;

    private int itemId; // 주문 상품 번호
    private String itemName; // 주문 상품 이름
    private int itemPrice; // 주문 상품 가격
    private int itemCount; // 주문 상품 수량
    private int itemTotalPrice; // 주문 상품 전체 가격
    private int isCancle; // 주문 취소 여부 ( 0 - 주문 완료 / 1 - 주문 취소 )

    @OneToOne
    @JoinColumn(name = "saleItem_id")
    private SaleItem saleItem; // 주문 상품에 매핑되는 판매 상품

    // 장바구니 전체 주문
    public static OrderItem createOrderItem(int itemId, Member member, CartItem cartItem, SaleItem saleItem) {
        OrderItem orderItem = new OrderItem();

        orderItem.setItemId(itemId);
        orderItem.setMember(member);
        orderItem.setItemName(cartItem.getItem().getName());
        orderItem.setItemPrice(cartItem.getItem().getPrice());
        orderItem.setItemCount(cartItem.getCount());
        orderItem.setItemTotalPrice(cartItem.getItem().getPrice() * cartItem.getCount());
        orderItem.setSaleItem(saleItem);

        return orderItem;
    }

    // 상품 개별 주문
    public static OrderItem createOrderItem(int itemId, Member member, Item item, int count, Order order, SaleItem saleItem) {
        OrderItem orderItem = new OrderItem();

        orderItem.setItemId(itemId);
        orderItem.setMember(member);
        orderItem.setOrder(order);
        orderItem.setItemName(item.getName());
        orderItem.setItemPrice(item.getPrice());
        orderItem.setItemCount(count);
        orderItem.setItemTotalPrice(item.getPrice() * count);
        orderItem.setSaleItem(saleItem);

        return orderItem;
    }
}
