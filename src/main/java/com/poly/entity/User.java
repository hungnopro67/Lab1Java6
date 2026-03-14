package com.poly.entity; // Đổi thành tên package của bạn

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "J6users")
public class User {
    @Id
    private String username;
    private String password;
    private boolean enabled;
    
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<UserRole> userRoles;
}