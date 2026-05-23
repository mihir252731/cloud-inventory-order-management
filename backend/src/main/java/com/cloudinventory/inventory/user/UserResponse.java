package com.cloudinventory.inventory.user;

import java.util.Set;

public record UserResponse(
        Long id,
        String username,
        String email,
        String fullName,
        boolean active,
        Set<RoleName> roles
) {
    public static UserResponse from(AppUser user) {
        Set<RoleName> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(java.util.stream.Collectors.toSet());
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getFullName(), user.isActive(), roleNames);
    }
}
