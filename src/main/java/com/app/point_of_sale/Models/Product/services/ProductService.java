package com.app.point_of_sale.Models.Product.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.app.point_of_sale.Models.Product.ProductRepository;
import com.app.point_of_sale.Models.Product.dto.request.ProductRequest;
import com.app.point_of_sale.Models.Product.dto.response.ProductResponse;

import jakarta.transaction.Transactional;

@Service
public class ProductService {
    ProductRepository productRepository;

    ProductService(
        ProductRepository productRepository
    ) {
        this.productRepository = productRepository;
    }
}
 