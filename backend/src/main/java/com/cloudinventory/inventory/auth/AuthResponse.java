package com.cloudinventory.inventory.auth;

import java.util.Set;

import com.cloudinventory.inventory.user.RoleName;

public record AuthResponse(
        String token,
        String username,
        String fullName,
        Set<RoleName> roles
) {
}
