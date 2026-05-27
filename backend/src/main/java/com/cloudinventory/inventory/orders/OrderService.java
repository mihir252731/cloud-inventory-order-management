package com.cloudinventory.inventory.orders;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloudinventory.inventory.customer.CustomerAccountService;
import com.cloudinventory.inventory.inventory.InventoryTransaction;
import com.cloudinventory.inventory.inventory.InventoryTransactionRepository;
import com.cloudinventory.inventory.inventory.InventoryTransactionType;
import com.cloudinventory.inventory.integration.salesforce.SalesforceIntegrationService;
import com.cloudinventory.inventory.product.Product;
import com.cloudinventory.inventory.product.ProductRepository;
import com.cloudinventory.inventory.product.ProductService;
import com.cloudinventory.inventory.supplier.Supplier;
import com.cloudinventory.inventory.supplier.SupplierService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class OrderService {

    private final CustomerOrderRepository customerOrderRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final SupplierService supplierService;
    private final InventoryTransactionRepository inventoryTransactionRepository;
    private final CustomerAccountService customerAccountService;
    private final SalesforceIntegrationService salesforceIntegrationService;

    public OrderService(
            CustomerOrderRepository customerOrderRepository,
            PurchaseOrderRepository purchaseOrderRepository,
            ProductService productService,
            ProductRepository productRepository,
            SupplierService supplierService,
            InventoryTransactionRepository inventoryTransactionRepository,
            CustomerAccountService customerAccountService,
            SalesforceIntegrationService salesforceIntegrationService
    ) {
        this.customerOrderRepository = customerOrderRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.productService = productService;
        this.productRepository = productRepository;
        this.supplierService = supplierService;
        this.inventoryTransactionRepository = inventoryTransactionRepository;
        this.customerAccountService = customerAccountService;
        this.salesforceIntegrationService = salesforceIntegrationService;
    }

    @Transactional
    @CacheEvict(value = {"products", "lowStockProducts"}, allEntries = true)
    public CustomerOrder createCustomerOrder(CustomerOrderRequest request) {
        CustomerOrder order = new CustomerOrder();
        order.setOrderNumber("SO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setCustomerName(request.customerName());
        order.setCustomerEmail(request.customerEmail());
        order.setStatus(OrderStatus.PENDING);

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderLineRequest itemRequest : request.items()) {
            Product product = productService.getProductById(itemRequest.productId());
            if (product.getStockQuantity() < itemRequest.quantity()) {
                throw new IllegalArgumentException("Insufficient stock for " + product.getName());
            }

            product.setStockQuantity(product.getStockQuantity() - itemRequest.quantity());
            productRepository.save(product);

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemRequest.quantity());
            item.setUnitPrice(product.getUnitPrice());
            item.setLineTotal(product.getUnitPrice().multiply(BigDecimal.valueOf(itemRequest.quantity())));
            order.getItems().add(item);
            totalAmount = totalAmount.add(item.getLineTotal());

            InventoryTransaction transaction = new InventoryTransaction();
            transaction.setProduct(product);
            transaction.setTransactionType(InventoryTransactionType.CUSTOMER_ORDER_ALLOCATION);
            transaction.setQuantityChange(-itemRequest.quantity());
            transaction.setResultingStock(product.getStockQuantity());
            transaction.setReferenceType(order.getOrderNumber());
            transaction.setPerformedBy("system");
            transaction.setNotes("Inventory reserved for customer order");
            inventoryTransactionRepository.save(transaction);
        }

        order.setTotalAmount(totalAmount);
        CustomerOrder savedOrder = customerOrderRepository.save(order);
        customerAccountService.getOrCreate(savedOrder.getCustomerName(), savedOrder.getCustomerEmail());
        salesforceIntegrationService.pushOrder(savedOrder.getId());
        return savedOrder;
    }

    public List<CustomerOrder> getCustomerOrders() {
        return customerOrderRepository.findAll();
    }

    @Transactional
    public CustomerOrder updateOrderStatus(Long orderId, OrderStatusUpdateRequest request) {
        CustomerOrder order = customerOrderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found: " + orderId));
        order.setStatus(request.status());
        return customerOrderRepository.save(order);
    }

    public PurchaseOrder createPurchaseOrder(PurchaseOrderRequest request) {
        Supplier supplier = supplierService.getSupplierById(request.supplierId());
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setPoNumber("PO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        purchaseOrder.setSupplier(supplier);
        purchaseOrder.setRequestedBy(request.requestedBy());
        purchaseOrder.setStatus(PurchaseOrderStatus.APPROVED);
        purchaseOrder.setTotalAmount(request.totalAmount());
        purchaseOrder.setNotes(request.notes());
        return purchaseOrderRepository.save(purchaseOrder);
    }

    public List<PurchaseOrder> getPurchaseOrders() {
        return purchaseOrderRepository.findAll();
    }
}
