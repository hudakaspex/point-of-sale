package com.app.point_of_sale.Models.Promotion;

import java.math.BigDecimal;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Promotions")
public class Promotion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @CreationTimestamp()
    private Long createdAt;

    @UpdateTimestamp()
    private Long updatedAt;

    private Long startDate;

    private Long endDate;

    private String description;

    //usage limit of promotion
    private Integer limit;

    private DiscountType discountType;

    private BigDecimal discountValue;

    private BigDecimal maxDiscount;

    private BigDecimal minOrderAmount;
}
