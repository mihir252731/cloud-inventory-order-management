package com.cloudinventory.inventory.product;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Override
    @EntityGraph(attributePaths = {"supplier"})
    List<Product> findAll();

    @EntityGraph(attributePaths = {"supplier"})
    List<Product> findByStockQuantityLessThanEqual(Integer threshold);
}
