package com.poly.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	// LƯU Ý: Ở Bài 3, chúng ta đã xóa hàm @Bean UserDetailsService.
	// Spring Security sẽ tự động tìm và sử dụng class DaoUserDetailsManager (có gắn @Service) để xác thực.

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// Bỏ cấu hình mặc định CSRF và CORS
		http.csrf(config -> config.disable()).cors(config -> config.disable());
        
		// Phân quyền truy xuất theo vai trò 
		http.authorizeHttpRequests(config -> {
			config.requestMatchers("/poly/url1").authenticated(); 
			config.requestMatchers("/poly/url2").hasRole("USER");
			config.requestMatchers("/poly/url3").hasRole("ADMIN");
			config.requestMatchers("/poly/url4").hasAnyRole("USER", "ADMIN");
			config.anyRequest().permitAll(); 
		});

        // Cấu hình xử lý từ chối truy xuất (Access Denied)
		http.exceptionHandling(config -> {
			config.accessDeniedPage("/access-denied.html");
		});

		// Các cấu hình Form đăng nhập
		http.formLogin(config -> {
			config.loginPage("/login/form");
			config.loginProcessingUrl("/login/check");
			config.defaultSuccessUrl("/", false);
			config.failureUrl("/login/failure");
			config.permitAll();
			config.usernameParameter("username");
			config.passwordParameter("password");
		});
        
		// Cấu hình Ghi nhớ tài khoản (Remember Me)
		http.rememberMe(config -> {
			config.tokenValiditySeconds(3 * 24 * 60 * 60); 
			config.rememberMeCookieName("remember-me");
			config.rememberMeParameter("remember-me");
		});
        
		// Cấu hình Đăng xuất (Logout)
		http.logout(config -> {
			config.logoutUrl("/logout");
			config.logoutSuccessUrl("/login/exit");
			config.clearAuthentication(true);
			config.invalidateHttpSession(true);
			config.deleteCookies("remember-me");
		});
        
		return http.build();
	}
}