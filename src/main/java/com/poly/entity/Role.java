package com.poly.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor

@Data
@Builder
@Entity
@Table(name = "J6roles")
public class Role {
    @Id
    private String id;
    private String name;
    
    @OneToMany(mappedBy = "role")
    private List<UserRole> userRoles;
}