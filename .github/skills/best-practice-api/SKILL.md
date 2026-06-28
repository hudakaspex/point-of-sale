---
name: best-practice-api
description: 'Generate REST API controllers and DTOs for Spring Boot projects. Use when: creating new API endpoints, adding CRUD operations, designing request/response schemas, implementing validation patterns. Applies to Spring Boot REST APIs with JPA entities and JWT auth.'
user-invocable: true
---

# Best Practice REST API — Spring Boot

## When to Use
- Creating a new REST controller for a domain entity
- Designing request/response DTOs with validation
- Adding CRUD endpoints to an existing controller
- Implementing consistent error handling for API endpoints
- Applying security annotations to controller methods

**Do NOT use** for: modifying entities/repositories/services (separate concerns), OpenAPI/Swagger configuration, frontend API clients.

## Project Context

This is a **Spring Boot 4.0.5** POS application with:
- **Java 21**, Maven build, PostgreSQL database
- **JWT auth** (stateless, Bearer token) — all endpoints except `/api/auth/**` require authentication
- `@EnableMethodSecurity(securedEnabled = true)` already configured — use `@Secured` for role/permission checks
- **Entities use UUID primary keys** (`GenerationType.UUID`)
- **Lombok** (`@Getter`, `@Setter`) used throughout
- **Jakarta Validation** (`spring-boot-starter-validation`) available
- Package structure: `com.app.point_of_sale.Models.<Entity>`

### Existing Domain Entities

| Entity | Package | Key Fields |
|--------|---------|------------|
| `User` | `Models.User` | id, fullname, email, password, roles |
| `Product` | `Models.Product` | id, name, description, discontinued, category |
| `Category` | `Models.Category` | id, name, description, parent, categories |
| `Customer` | `Models.Customer` | extends `Person` (id, firstName, lastName, phone, email, birthDate) |
| `Employee` | `Models.Employee` | extends `Person` (same as Customer) |
| `Variant` | `Models.Variant` | id, name, sku, discontinued, price, product |
| `Role` | `Models.User.Role` | id, name, description, isActive, permissions |
| `Permission` | `Models.User.Permission` | id, name, description, isActive |

### Security

See [SecurityHttpConfiguration](../../../main/java/com/app/point_of_sale/Models/User/Configuration/SecurityHttpConfiguration.java):
- `/api/auth/**` — public (permit all)
- Everything else — requires JWT authentication
- `@Secured("PERMISSION_NAME")` — use on controller methods for fine-grained access control
- Exising permissions follow the pattern: `PERMISSION_CREATE`, `PERMISSION_READ`, `PERMISSION_UPDATE`, `PERMISSION_DELETE`

Repository pattern: `JpaRepository<Entity, UUID>` with custom query methods as needed.

## Procedure

### 1. Create the DTOs

For each domain entity, create a package structure:
```
Models/<Entity>/
├── dto/
│   ├── request/
│   │   └── <Entity>Request.java
│   └── response/
│       └── <Entity>Response.java
```

#### Request DTO Pattern

```java
package com.app.point_of_sale.Models.<Entity>.dto.request;

import java.util.UUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class <Entity>Request {
    @NotBlank(message = "Name is required")
    @Size(max = 255)
    private String name;

    @Size(max = 1000)
    private String description;

    // For update operations, include the ID
    private UUID id;
    
    // Entity-specific fields — add based on the target entity
    // Relationship references use the related entity's ID (not the entity itself)
    // Example: private UUID categoryId;
    
    // Boolean fields default to null so partial updates work
    // Example: private Boolean discontinued;
}
```

**Validation annotation guidelines:**
- `@NotBlank` — String fields that must not be null/blank
- `@NotNull` — Required fields (UUID references, booleans, enums)
- `@Email` — Email address fields
- `@Positive` / `@PositiveOrZero` — Numeric fields (prices, quantities)
- `@Size(max = ...)` — String length constraints
- `@Future` / `@Past` — Date fields if applicable

#### Response DTO Pattern

```java
package com.app.point_of_sale.Models.<Entity>.dto.response;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class <Entity>Response {
    private UUID id;
    private String name;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;

    // Entity-specific fields
    // Relationship fields can include the related entity's ID or a nested summary DTO
    // Example: private UUID categoryId;
    // Example: private String categoryName;

    /**
     * Factory method to create a response DTO from an entity.
     * Maps entity fields to the response DTO.
     */
    public static <Entity>Response fromEntity(<Entity> entity) {
        <Entity>Response response = new <Entity>Response();
        response.setId(entity.getId());
        response.setName(entity.getName());
        // ... map remaining fields
        return response;
    }
}
```

### 2. Create the Controller

Place controllers in:
```
Models/<Entity>/controller/<Entity>Controller.java
```

#### CRUD Controller Pattern

```java
package com.app.point_of_sale.Models.<Entity>.controller;

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

import com.app.point_of_sale.Models.<Entity>.<Entity>;
import com.app.point_of_sale.Models.<Entity>.<Entity>Repository;
import com.app.point_of_sale.Models.<Entity>.dto.request.<Entity>Request;
import com.app.point_of_sale.Models.<Entity>.dto.response.<Entity>Response;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/<endpoint-path>")
@Validated
public class <Entity>Controller {

    private final <Entity>Repository <entity>Repository;

    public <Entity>Controller(<Entity>Repository <entity>Repository) {
        this.<entity>Repository = <entity>Repository;
    }

    // --- CREATE ---
    @PostMapping
    @Secured("PERMISSION_CREATE")
    public ResponseEntity<<Entity>Response> create(@Valid @RequestBody <Entity>Request request) {
        <Entity> entity = new <Entity>();
        // map request to entity
        // entity.setName(request.getName());
        <Entity> saved = <entity>Repository.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(<Entity>Response.fromEntity(saved));
    }

    // --- READ ALL ---
    @GetMapping
    @Secured("PERMISSION_READ")
    public ResponseEntity<List<<Entity>Response>> findAll() {
        List<<Entity>> entities = <entity>Repository.findAll();
        List<<Entity>Response> responses = entities.stream()
                .map(<Entity>Response::fromEntity)
                .toList();
        return ResponseEntity.ok(responses);
    }

    // --- READ ONE ---
    @GetMapping("/{id}")
    @Secured("PERMISSION_READ")
    public ResponseEntity<<Entity>Response> findById(@PathVariable UUID id) {
        return <entity>Repository.findById(id)
                .map(entity -> ResponseEntity.ok(<Entity>Response.fromEntity(entity)))
                .orElse(ResponseEntity.notFound().build());
    }

    // --- UPDATE ---
    @PutMapping("/{id}")
    @Secured("PERMISSION_UPDATE")
    public ResponseEntity<<Entity>Response> update(
            @PathVariable UUID id,
            @Valid @RequestBody <Entity>Request request) {
        return <entity>Repository.findById(id)
                .map(existing -> {
                    // map request fields to existing entity
                    // existing.setName(request.getName());
                    <Entity> saved = <entity>Repository.save(existing);
                    return ResponseEntity.ok(<Entity>Response.fromEntity(saved));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // --- DELETE ---
    @DeleteMapping("/{id}")
    @Secured("PERMISSION_DELETE")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!<entity>Repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        <entity>Repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
```

### 3. HTTP Status Code Conventions

| Operation | HTTP Method | Success Status | Notes |
|-----------|-------------|---------------|-------|
| Create | `POST` | `201 Created` | Return the created resource in body |
| Read all | `GET` | `200 OK` | Return list (empty list, not 404) |
| Read one | `GET` | `200 OK` / `404 Not Found` | 404 if ID doesn't exist |
| Update | `PUT` | `200 OK` / `404 Not Found` | Full resource replacement |
| Delete | `DELETE` | `204 No Content` / `404 Not Found` | No response body |

### 4. Endpoint Path Conventions

Use plural, kebab-case endpoints:
```
/api/v1/products
/api/v1/categories
/api/v1/customers
/api/v1/employees
/api/v1/variants
/api/v1/users
/api/v1/roles
/api/v1/permissions
```

Nested resources (when appropriate):
```
/api/v1/products/{productId}/variants
```

### 5. Validation & Error Handling

- Use `@Valid` on all `@RequestBody` parameters
- Return validation errors as `400 Bad Request` with field-level details
- For required path variables, let the framework handle `404` — no need to manually validate
- The `ExceptionHandler` package at `com.app.point_of_sale.ExceptionHandler` is available for `@ControllerAdvice`
- When entity not found, return `ResponseEntity.notFound().build()` consistently

### 6. Security Integration

- All endpoints (except `/api/auth/**`) require JWT authentication automatically
- Use `@Secured("PERMISSION_NAME")` for method-level authorization
- Existing permission conventions: `PERMISSION_CREATE`, `PERMISSION_READ`, `PERMISSION_UPDATE`, `PERMISSION_DELETE`
- For endpoints that should be accessible to any authenticated user, omit `@Secured` (authentication alone is sufficient)
- Never use `@PreAuthorize` with SpEL unless specific runtime checks are needed — prefer `@Secured` for clarity

### 7. Complete Example: Product API

#### Request DTO: `Models/Product/dto/request/ProductRequest.java`
```java
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
```

#### Response DTO: `Models/Product/dto/response/ProductResponse.java`
```java
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
```

#### Controller: `Models/Product/controller/ProductController.java`
```java
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
```

## What This Skill Produces

When invoked, this skill generates:
1. A `*Request.java` DTO with Jakarta validation annotations
2. A `*Response.java` DTO with a `fromEntity()` factory method
3. A `*Controller.java` REST controller with full CRUD endpoints
4. All endpoints follow consistent URL patterns, HTTP status codes, and security annotations

## Example Prompts to Try

After creating this skill, test it with prompts like:
- "Using best-practice-api, create a Category API with controller and DTOs"
- "Generate Product API endpoints following best-practice-api"
- "Add a Variant controller using the best-practice-api skill"
- "Create Customer and Employee DTOs and controllers following best-practice-api"

## Related Customizations

- Consider creating a companion skill for **Service & Repository** layer patterns
- Consider `ExceptionHandler` instructions for `@ControllerAdvice` standards
- Consider workspace instructions (`copilot-instructions.md`) if these patterns should apply by default
