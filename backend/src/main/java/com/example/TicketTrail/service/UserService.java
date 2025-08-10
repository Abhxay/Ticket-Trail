package com.example.TicketTrail.service;

import com.example.TicketTrail.dto.RegisterRequest;
import com.example.TicketTrail.dto.UserResponse;
import com.example.TicketTrail.entity.Role;
import com.example.TicketTrail.entity.User;
import com.example.TicketTrail.repository.RoleRepository;
import com.example.TicketTrail.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        createRoleIfNotFound("ROLE_USER");
        createRoleIfNotFound("ROLE_ADMIN");
        createRoleIfNotFound("ROLE_SUPER_ADMIN");

        // -- Seed default Superadmin user (if missing)
        if (userRepository.findByUsername("Superadmin").isEmpty()) {
            Role superAdminRole = roleRepository.findByName("ROLE_SUPER_ADMIN")
                    .orElseThrow(() -> new RuntimeException("ROLE_SUPER_ADMIN role not found"));
            User superadmin = new User();
            superadmin.setUsername("Superadmin");
            superadmin.setPassword(passwordEncoder.encode("superadmin"));
            superadmin.setRoles(Collections.singleton(superAdminRole));
            userRepository.save(superadmin);
            System.out.println("✅ Superadmin created with username 'Superadmin' and password 'superadmin'");
        }

        // -- Seed default Admin_1 user (if missing)
        if (userRepository.findByUsername("Admin_1").isEmpty()) {
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("ROLE_ADMIN role not found"));
            User admin1 = new User();
            admin1.setUsername("Admin_1");
            admin1.setPassword(passwordEncoder.encode("admin123"));
            admin1.setRoles(Collections.singleton(adminRole));
            userRepository.save(admin1);
            System.out.println("✅ Admin user created with username 'Admin_1' and password 'admin123'");
        }

        // -- Seed default normal user 'user' (if missing)
        if (userRepository.findByUsername("user").isEmpty()) {
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));
            User normalUser = new User();
            normalUser.setUsername("user");
            normalUser.setPassword(passwordEncoder.encode("user123"));
            normalUser.setRoles(Collections.singleton(userRole));
            userRepository.save(normalUser);
            System.out.println("✅ Normal user created with username 'user' and password 'user123'");
        }
    }

    private void createRoleIfNotFound(String roleName) {
        roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.save(new Role(roleName)));
    }

    public User registerUserWithRoles(RegisterRequest request) {
        userRepository.findByUsername(request.getUsername()).ifPresent(u -> {
            throw new IllegalArgumentException("Username already exists.");
        });

        Set<Role> roles;
        if (request.getRoles() == null || request.getRoles().isEmpty()) {
            Role defaultRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new IllegalStateException("ROLE_USER not found"));
            roles = Collections.singleton(defaultRole);
        } else {
            roles = request.getRoles().stream()
                    .map(roleName -> roleRepository.findByName(roleName)
                            .orElseThrow(() -> new IllegalStateException("Role not found: " + roleName)))
                    .collect(Collectors.toSet());
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(roles);

        return userRepository.save(user);
    }

    public User registerUser(RegisterRequest request) {
        userRepository.findByUsername(request.getUsername())
                .ifPresent(u -> { throw new IllegalArgumentException("Username already exists."); });

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("ROLE_USER not found"));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Collections.singleton(userRole));

        return userRepository.save(user);
    }

    public User registerAdmin(RegisterRequest request) {
        userRepository.findByUsername(request.getUsername())
                .ifPresent(u -> { throw new IllegalArgumentException("Username already exists."); });

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new IllegalStateException("ROLE_ADMIN not found"));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Collections.singleton(adminRole));

        return userRepository.save(user);
    }

    public void changeUsername(Long userId, String newUsername) {
        if (userRepository.findByUsername(newUsername).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(newUsername); // roles remain unchanged
        userRepository.save(user);
    }

    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (currentPassword != null && !passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(newPassword)); // roles remain unchanged
        userRepository.save(user);
    }

    // Role management
    public void addAdminRoleToUser(Long userId) { addRole(userId, "ROLE_ADMIN"); }
    public void removeAdminRoleFromUser(Long userId) { removeRole(userId, "ROLE_ADMIN"); }
    public void addSuperAdminRoleToUser(Long userId) { addRole(userId, "ROLE_SUPER_ADMIN"); }
    public void removeSuperAdminRoleFromUser(Long userId) { removeRole(userId, "ROLE_SUPER_ADMIN"); }

    private void addRole(Long userId, String roleName) {
        User user = findUser(userId);
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException(roleName + " not found"));
        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
            userRepository.save(user);
        }
    }

    private void removeRole(Long userId, String roleName) {
        User user = findUser(userId);
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException(roleName + " not found"));
        if (user.getRoles().contains(role)) {
            user.getRoles().remove(role);
            userRepository.save(user);
        }
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<UserResponse> getAllUsersWithRoles() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
                ))
                .collect(Collectors.toList());
    }
}
