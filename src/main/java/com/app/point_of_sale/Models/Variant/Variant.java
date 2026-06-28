package com.app.point_of_sale.Models.Variant;

import java.math.BigDecimal;
import java.sql.Types;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;

import com.app.point_of_sale.Models.AttributeValue.AttributeValue;
import com.app.point_of_sale.Models.Product.Product;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Variant {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String sku;

    private Boolean isActive;

    @Column(nullable = false)
    private BigDecimal price;

    @CreationTimestamp
    @JdbcTypeCode(Types.BIGINT)
    private Instant createdAt;

    @UpdateTimestamp
    @JdbcTypeCode(Types.BIGINT)
    private Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToMany(
        cascade = CascadeType.REMOVE,
        fetch = FetchType.LAZY
    )
    private List<AttributeValue> attributeValues;
}
