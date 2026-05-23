package com.cloudinventory.inventory.auth;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cloudinventory.inventory.security.JwtService;
import com.cloudinventory.inventory.user.AppUser;
import com.cloudinventory.inventory.user.Role;
import com.cloudinventory.inventory.user.RoleName;
import com.cloudinventory.inventory.user.RoleRepository;
import com.cloudinventory.inventory.user.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Role role = roleRepository.findByName(request.role())
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        AppUser user = new AppUser();
        user.setFullName(request.fullName());
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.getRoles().add(role);

        AppUser savedUser = userRepository.save(user);
        return buildAuthResponse(savedUser);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        AppUser user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        return buildAuthResponse(user);
    }

    private AuthResponse buildAuthResponse(AppUser user) {
        Set<RoleName> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        String token = jwtService.generateToken(
                user.getUsername(),
                Map.of(
                        "roles", roles.stream().map(Enum::name).toList(),
                        "fullName", user.getFullName()
                )
        );

        return new AuthResponse(token, user.getUsername(), user.getFullName(), roles);
    }
}
