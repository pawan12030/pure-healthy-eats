package com.example.purehealthyeats.service;

import com.example.purehealthyeats.model.Role;
import com.example.purehealthyeats.model.User;
import com.example.purehealthyeats.repository.RoleRepository;
import com.example.purehealthyeats.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public User createUser(User user) {
        try {
            // Check if user already exists
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new RuntimeException("User with email " + user.getEmail() + " already exists");
            }
            
            // Encode password
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            
            // Set default role
            Set<Role> roles = new HashSet<>();
            Role userRole = roleRepository.findByName("USER")
                .orElseGet(() -> {
                    Role newRole = new Role("USER");
                    return roleRepository.save(newRole);
                });
            roles.add(userRole);
            user.setRoles(roles);
            
            // Set default values
            user.setActive(true);
            
            return userRepository.save(user);
            
        } catch (Exception e) {
            System.err.println("Error creating user: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create user: " + e.getMessage());
        }
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public Optional<User> findByEmailAndActive(String email) {
        return userRepository.findByEmailAndActiveTrue(email);
    }
    
    public User save(User user) {
        return userRepository.save(user);
    }
    
    // Method to initialize default roles
    @Transactional
    public void initializeRoles() {
        if (!roleRepository.findByName("USER").isPresent()) {
            roleRepository.save(new Role("USER"));
        }
        if (!roleRepository.findByName("ADMIN").isPresent()) {
            roleRepository.save(new Role("ADMIN"));
        }
    }
}