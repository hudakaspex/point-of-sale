package com.app.point_of_sale.Models.Product.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {
    @NotBlank(message = "Product name is required")
    @Size(max = 255)
    private String name;

    @Size(max = 1000)
    private String description;

    private Boolean discontinued;

    private UUID categoryId;
}
