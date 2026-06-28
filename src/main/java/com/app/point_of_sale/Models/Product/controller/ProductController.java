package com.app.point_of_sale.Models.Product.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.point_of_sale.Models.Category.Category;
import com.app.point_of_sale.Models.Category.CategoryRepository;
import com.app.point_of_sale.Models.Product.Product;
import com.app.point_of_sale.Models.Product.ProductRepository;
import com.app.point_of_sale.Models.Product.dto.request.ProductRequest;
import com.app.point_of_sale.Models.Product.dto.response.ProductResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/products")
@Validated
public class ProductController {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductController(ProductRepository productRepository,
                             CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @PostMapping
    @Secured("PERMISSION_CREATE")
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setDiscontinued(request.getDiscontinued() != null ? request.getDiscontinued() : false);

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }

        Product saved = productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ProductResponse.fromEntity(saved));
    }

    @GetMapping
    @Secured("PERMISSION_READ")
    public ResponseEntity<List<ProductResponse>> findAll() {
        List<Product> products = productRepository.findAll();
        List<ProductResponse> responses = products.stream()
                .map(ProductResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    @Secured("PERMISSION_READ")
    public ResponseEntity<ProductResponse> findById(@PathVariable UUID id) {
        return productRepository.findById(id)
                .map(product -> ResponseEntity.ok(ProductResponse.fromEntity(product)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Secured("PERMISSION_UPDATE")
    public ResponseEntity<ProductResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody ProductRequest request) {
        return productRepository.findById(id)
                .map(existing -> {
                    existing.setName(request.getName());
                    existing.setDescription(request.getDescription());
                    existing.setDiscontinued(request.getDiscontinued());

                    if (request.getCategoryId() != null) {
                        Category category = categoryRepository.findById(request.getCategoryId())
                                .orElseThrow(() -> new RuntimeException("Category not found"));
                        existing.setCategory(category);
                    } else {
                        existing.setCategory(null);
                    }

                    Product saved = productRepository.save(existing);
                    return ResponseEntity.ok(ProductResponse.fromEntity(saved));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Secured("PERMISSION_DELETE")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!productRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
