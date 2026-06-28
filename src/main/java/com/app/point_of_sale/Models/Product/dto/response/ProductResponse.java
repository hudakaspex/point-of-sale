package com.app.point_of_sale.Models.Product.dto.response;

import java.time.Instant;
import java.util.UUID;

import com.app.point_of_sale.Models.Product.Product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponse {
    private UUID id;
    private String name;
    private String description;
    private Boolean discontinued;
    private Instant createdAt;
    private Instant updatedAt;
    private UUID categoryId;
    private String categoryName;

    public static ProductResponse fromEntity(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setDiscontinued(product.getDiscontinued());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
        if (product.getCategory() != null) {
            response.setCategoryId(product.getCategory().getId());
            response.setCategoryName(product.getCategory().getName());
        }
        return response;
    }
}
