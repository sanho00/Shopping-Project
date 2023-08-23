package com.shopping.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "memberId")
    private Member member;

    private int count;

    @OneToMany(mappedBy = "cart")
    private List<CartItem> cartItems = new ArrayList<>();

    public static Cart createCart(Member member) {
        Cart cart = new Cart();
        cart.setCount(0);
        cart.setMember(member);
        return cart;
    }
}
