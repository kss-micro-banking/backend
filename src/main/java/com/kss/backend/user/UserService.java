package com.kss.backend.user;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

/**
 * UserService
 */
@Service
public record UserService(UserRepository userRepository) {

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User findById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<User> findUsers(User.Role role) {
        if (role != null) {
            return userRepository.findByRole(role);
        }

        return userRepository.findAll();
    }
}
