package com.app.point_of_sale.Models.User.Role;

import java.sql.Types;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;

import com.app.point_of_sale.Models.User.Permission.Permission;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "roles")
@Getter
@Setter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column()
    private String description;

    @Column()
    private Boolean isActive;

    @CreationTimestamp
    @JdbcTypeCode(Types.BIGINT)
    @Column(updatable = false, nullable = false)
    private Instant createdAt;

    @ManyToMany()
    @JoinTable(
        name = "role_permissions",
        joinColumns = @JoinColumn(name= "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private List<Permission> permissions;
}
