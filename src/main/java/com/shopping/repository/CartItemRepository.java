package com.shopping.repository;

import com.shopping.domain.Cart;
import com.shopping.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    CartItem findByCartIdAndItemId(int cartId, int itemId);
    CartItem findCartItemById(int id);
    List<CartItem> findCartItemByItemId(int id);

}
