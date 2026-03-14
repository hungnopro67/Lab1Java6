package com.poly.dao;

import com.poly.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleDAO extends JpaRepository<UserRole, Long> {
}