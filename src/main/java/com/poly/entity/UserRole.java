package com.poly.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "J6userroles")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "username")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "roleid")
    private Role role;
}