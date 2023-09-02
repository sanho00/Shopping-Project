package com.shopping.repository;

import com.shopping.domain.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem, Integer> {

    List<SaleItem> findSaleItemBySellerId(int sellerId);
    List<SaleItem> findAll();
}
