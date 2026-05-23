package com.cloudinventory.inventory.orders;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    long countByStatus(PurchaseOrderStatus status);
}
