package com.shopping.repository;

import com.shopping.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    List<OrderItem> findOrderItemByMemberId(int memberId);
    List<OrderItem> findAll();
    OrderItem findOrderItemById(int orderItemId);
}
