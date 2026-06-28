package com.app.point_of_sale.Models.User.dto.response;

import java.time.Instant;
import java.util.UUID;

import com.app.point_of_sale.Models.User.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private UUID id;
    private String fullname;
    private String email;
    private Instant createdAt;
    private Instant updatedAt;

    /**
     * Factory method to create a UserResponse DTO from a User entity.
     * Excludes sensitive fields (password) and relationship data (roles).
     */
    public static UserResponse fromEntity(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFullname(user.getFullname());
        response.setEmail(user.getEmail());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}
