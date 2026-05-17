package com.app.point_of_sale.Models.Customer;

import com.app.point_of_sale.Models.Person.Person;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "customers")
public class Customer extends Person {
}
