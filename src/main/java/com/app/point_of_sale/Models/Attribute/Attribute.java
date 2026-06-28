package com.app.point_of_sale.Models.Attribute;

import java.util.List;
import java.util.UUID;

import com.app.point_of_sale.Models.AttributeValue.AttributeValue;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Attribute {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; 

    @Column
    private String name;

    @OneToMany(
        mappedBy = "attribute",
        cascade = CascadeType.REMOVE,
        fetch = FetchType.LAZY
    )
    private List<AttributeValue> values;
}
