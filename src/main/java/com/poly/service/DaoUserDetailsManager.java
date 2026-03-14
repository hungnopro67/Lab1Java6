package com.poly.service;

import com.poly.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service // Đánh dấu đây là Service để Spring tự động quản lý
public class DaoUserDetailsManager implements UserDetailsService {

    @Autowired
    private UserDAO dao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Tìm user trong CSDL
        com.poly.entity.User user = dao.findById(username).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        
        // 2. Lấy mật khẩu
        String password = user.getPassword();
        
        // 3. Lấy danh sách Role.
        // Hàm substring(5) dùng để cắt bỏ chữ "ROLE_" trong CSDL, vì hàm .roles() của Spring sẽ tự động thêm lại "ROLE_" vào.
        String[] roles = user.getUserRoles().stream()
                .map(ur -> ur.getRole().getId().substring(5))
                .toArray(String[]::new);
                
        // 4. Trả về đối tượng UserDetails cho Spring Security
        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(password)
                .roles(roles)
                .build();
    }
}