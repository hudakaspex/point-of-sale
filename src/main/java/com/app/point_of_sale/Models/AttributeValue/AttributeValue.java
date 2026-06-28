package com.app.point_of_sale.Models.AttributeValue;

import java.util.UUID;

import com.app.point_of_sale.Models.Attribute.Attribute;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AttributeValue {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String value;

    @ManyToOne()
    private Attribute attribute;
}
