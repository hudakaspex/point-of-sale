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
import com.app.point_of_sale.Models.Product.services.ProductService;


@RestController
@RequestMapping("/api/v1/products")
@Validated
public class ProductController {
    ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }

    
}
