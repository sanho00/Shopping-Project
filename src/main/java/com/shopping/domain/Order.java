package com.shopping.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList) {
        Order order = new Order();
        order.setMember(member);

        for (OrderItem orderItem : orderItemList) {
            order.addOrderItem(orderItem);
        }
        return order;
    }

    public static Order createOrder(Member member) {
        Order order = new Order();
        order.setMember(member);
        return order;
    }
}
