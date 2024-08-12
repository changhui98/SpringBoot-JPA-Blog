package com.cos.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.security.auth.message.config.AuthConfig;

// 빈 등록 : 스프링 컨테이너에서 객체를 관리할 수 있게 하는 것
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true) // 특정 주소로 접근을 하면 권한 및 인증을 미리 체크하겠다는 뜻

/**
 * 스프링 시큐리티 버전 업그레이드 되면서, 위 두가지 어노테이션 제거.
 */

@Configuration // 빈 등록 IoC 관리
public class SecurityConfig {

	// ** spring security update : principalDetailService 제거

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration anthenAuthenticationConfiguration)
			throws Exception {
		return anthenAuthenticationConfiguration.getAuthenticationManager();
	}

	@Bean // IoC가 됨.
	public BCryptPasswordEncoder encodePWD() {

		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http.csrf().disable() // csrf 토큰 비활성화 (테스트시 걸어두는 게 좋음)
				.authorizeHttpRequests(
						auth -> auth.requestMatchers("/", "/auth/**", "/WEB-INF/**", "/js/**", "/css/**", "/image/**")
								.permitAll().anyRequest().authenticated());

		http.formLogin(f -> f.loginPage("/auth/loginForm").permitAll());

		return http.build();
	}

}
