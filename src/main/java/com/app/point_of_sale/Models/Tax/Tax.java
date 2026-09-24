package com.app.point_of_sale.Models.Tax;

import java.math.BigDecimal;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Tax {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @Column(precision = 5, scale = 4)
    private BigDecimal rate;

    private boolean isActive;
    
    @CreationTimestamp()
    private Long createdAt;

    @UpdateTimestamp()
    private Long updatedAt;
}
