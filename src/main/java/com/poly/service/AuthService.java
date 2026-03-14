package com.poly.service;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("auth")
public class AuthService {
    
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
    
    public String getUsername() {
        return this.getAuthentication().getName();
    }
    
    public List<String> getRoles() {
        return this.getAuthentication().getAuthorities().stream()
                .map(au -> au.getAuthority().substring(5)).toList();
    }
    
    public boolean isAuthenticated() {
        String username = this.getUsername();
        return (username != null && !username.equals("anonymousUser"));
    }
    
    public boolean hasAnyRole(String... rolesToCheck) {
        var grantedRoles = this.getRoles();
        return Stream.of(rolesToCheck).anyMatch(role -> grantedRoles.contains(role));
    }
}