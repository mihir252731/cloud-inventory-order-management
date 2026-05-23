package com.cloudinventory.inventory.orders;

import jakarta.validation.constraints.NotNull;

public record OrderStatusUpdateRequest(@NotNull OrderStatus status) {
}
