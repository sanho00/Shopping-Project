package com.shopping.domain;

import lombok.*;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cartId")
    private Cart cart;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "itemId")
    private Item item;
    private int count;

    public static CartItem createCartItem(Cart cart, Item item, int amount) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(amount);
        return cartItem;
    }

    public void addCount(int count) {
        this.count += count;
        // 이미 담겨 있을 때 추가할 경우 수량 증가
    }
}
