package com.cloudinventory.inventory.orders;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

    @Override
    @EntityGraph(attributePaths = {"items", "items.product"})
    List<CustomerOrder> findAll();

    long countByStatusIn(List<OrderStatus> statuses);
}
