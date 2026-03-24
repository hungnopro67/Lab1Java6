package com.poly.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import com.poly.service.DaoUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, DaoUserDetailsManager dao) throws Exception {

		http.csrf(config -> config.disable()).cors(config -> config.disable());

		http.authorizeHttpRequests(config -> {
			config.requestMatchers("/poly/**").authenticated();
			config.anyRequest().permitAll();
		});

		http.formLogin(config -> {
			config.loginPage("/login/form");
			config.loginProcessingUrl("/login/check");
			config.defaultSuccessUrl("/", false);
			config.failureUrl("/login/failure");
			config.permitAll();
			config.usernameParameter("username");
			config.passwordParameter("password");
		});

		http.oauth2Login(config -> {
			config.loginPage("/login/form");

			config.successHandler((request, response, authentication) -> {

				DefaultOAuth2User oauthUser = (DefaultOAuth2User) authentication.getPrincipal();

				String email = oauthUser.getAttribute("email");

				UserDetails user = User.withUsername(email).password(passwordEncoder().encode("noop")).roles("USER")
						.build();

				Authentication newAuth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

				SecurityContextHolder.getContext().setAuthentication(newAuth);

				response.sendRedirect("/");
			});

			config.failureUrl("/login/failure");
		});

		http.rememberMe(config -> {
			config.tokenValiditySeconds(3 * 24 * 60 * 60);
			config.rememberMeCookieName("remember-me");
			config.rememberMeParameter("remember-me");
			config.userDetailsService(dao);
		});

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