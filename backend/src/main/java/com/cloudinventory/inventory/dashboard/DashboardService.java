package com.cloudinventory.inventory.dashboard;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cloudinventory.inventory.orders.CustomerOrderRepository;
import com.cloudinventory.inventory.orders.OrderStatus;
import com.cloudinventory.inventory.orders.PurchaseOrderRepository;
import com.cloudinventory.inventory.orders.PurchaseOrderStatus;
import com.cloudinventory.inventory.product.ProductRepository;
import com.cloudinventory.inventory.supplier.SupplierRepository;
import com.cloudinventory.inventory.user.UserRepository;

@Service
public class DashboardService {

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final CustomerOrderRepository customerOrderRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final UserRepository userRepository;

    public DashboardService(
            ProductRepository productRepository,
            SupplierRepository supplierRepository,
            CustomerOrderRepository customerOrderRepository,
            PurchaseOrderRepository purchaseOrderRepository,
            UserRepository userRepository
    ) {
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
        this.customerOrderRepository = customerOrderRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.userRepository = userRepository;
    }

    public DashboardSummaryResponse getSummary() {
        int totalStockUnits = Math.toIntExact(productRepository.sumStockQuantity());
        long lowStockItems = productRepository.countLowStockProducts();

        long openCustomerOrders = customerOrderRepository.countByStatusIn(
                List.of(OrderStatus.PENDING, OrderStatus.PROCESSING, OrderStatus.PICKED, OrderStatus.SHIPPED)
        );

        long pendingPurchaseOrders = purchaseOrderRepository.countByStatus(PurchaseOrderStatus.APPROVED)
                + purchaseOrderRepository.countByStatus(PurchaseOrderStatus.IN_TRANSIT);

        long activeUsers = userRepository.countByActiveTrue();

        return new DashboardSummaryResponse(
                productRepository.count(),
                supplierRepository.count(),
                totalStockUnits,
                lowStockItems,
                openCustomerOrders,
                pendingPurchaseOrders,
                activeUsers
        );
    }
}
