package com.app.point_of_sale.Models.Product.dto.request;

import java.math.BigDecimal;

import com.app.point_of_sale.Utils.Validators.SkuValidator;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class ProductVariantRequest {
    
    @SkuValidator
    private String sku;

    private BigDecimal price;
}
