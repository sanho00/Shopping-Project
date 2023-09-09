package com.shopping.domain;

import com.shopping.constant.Role;
import com.shopping.dto.MemberFormDto;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String username;
    private String password;
    private String email;
    private String name;

    //주소
    private String address;
    private String phoneNumber;
    private String role; // 권한

    @OneToOne(mappedBy = "member")
    private Cart cart; // 구매자 장바구니

    @OneToMany(mappedBy = "member")
    private List<Order> memberOrder = new ArrayList<>(); // 구매자 주문

    @OneToMany(mappedBy = "member")
    private List<OrderItem> memberOrderItem = new ArrayList<>(); // 구매자 주문 상품들

    @OneToMany(mappedBy = "seller")
    private List<Item> items = new ArrayList<>(); // 판매자가 가지고 있는 상품들

    @OneToMany(mappedBy = "seller")
    private List<SaleItem> sellerSaleItem = new ArrayList<>(); // 판매자의 판매 상품들

    @OneToMany(mappedBy = "seller")
    private List<Sale> sellerSale; // 판매

}
