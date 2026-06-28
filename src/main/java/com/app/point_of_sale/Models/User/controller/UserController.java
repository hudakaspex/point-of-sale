package com.app.point_of_sale.Models.User.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.point_of_sale.Models.User.UserRepository;
import com.app.point_of_sale.Models.User.dto.response.UserResponse;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * GET /api/v1/users/{id} — Fetch a user's detail data by ID.
     * Returns basic user info (id, fullname, email, timestamps).
     * Excludes password and roles for security.
     */
    @GetMapping("/{id}")
    @Secured("PERMISSION_READ")
    public ResponseEntity<UserResponse> findById(@PathVariable UUID id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(UserResponse.fromEntity(user)))
                .orElse(ResponseEntity.notFound().build());
    }
}
