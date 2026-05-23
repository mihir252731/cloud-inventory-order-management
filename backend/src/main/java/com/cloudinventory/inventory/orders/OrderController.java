package com.cloudinventory.inventory.orders;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_USER')")
    public CustomerOrder createCustomerOrder(@Valid @RequestBody CustomerOrderRequest request) {
        return orderService.createCustomerOrder(request);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_USER')")
    public List<CustomerOrder> getOrders() {
        return orderService.getCustomerOrders();
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public CustomerOrder updateStatus(@PathVariable Long id, @Valid @RequestBody OrderStatusUpdateRequest request) {
        return orderService.updateOrderStatus(id, request);
    }

    @PostMapping("/purchase")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public PurchaseOrder createPurchaseOrder(@Valid @RequestBody PurchaseOrderRequest request) {
        return orderService.createPurchaseOrder(request);
    }

    @GetMapping("/purchase")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_USER')")
    public List<PurchaseOrder> getPurchaseOrders() {
        return orderService.getPurchaseOrders();
    }
}
