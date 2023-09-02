package com.shopping.repository;

import com.shopping.domain.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Integer> {

    List<Sale> findAll();
    List<Sale> findSaleById(int id);
    Sale findBySellerId(int id);
}
