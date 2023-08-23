package com.shopping.repository;

import com.shopping.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    Cart findByMemberId(int id);
    Cart findCartById(int id);
    Cart findCartByMemberId(int id);
}
