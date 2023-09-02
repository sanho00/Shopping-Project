package com.shopping.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seller_id")
    private Member seller;

    @Builder.Default
    @OneToMany(mappedBy = "sale")
    private List<SaleItem> saleItems = new ArrayList<>();

    private int totalCount; // 총 판매 개수

    public static Sale createSale(Member seller) {
        Sale sale = new Sale();
        sale.setSeller(seller);
        sale.setTotalCount(0);
        return sale;
    }
}
