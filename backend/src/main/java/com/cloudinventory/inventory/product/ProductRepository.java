package com.cloudinventory.inventory.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Override
    @Query("select p from Product p join fetch p.supplier")
    List<Product> findAll();

    @Override
    @EntityGraph(attributePaths = {"supplier"})
    Optional<Product> findById(Long id);

    @Query("select p from Product p join fetch p.supplier where p.stockQuantity <= p.reorderLevel")
    List<Product> findLowStockProducts();

    @Query("select count(p) from Product p where p.stockQuantity <= p.reorderLevel")
    long countLowStockProducts();

    @Query("select coalesce(sum(p.stockQuantity), 0) from Product p")
    long sumStockQuantity();
}
