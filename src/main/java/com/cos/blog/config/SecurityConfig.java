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

	// 4. AuthenticationManager 메서드 생성 
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration anthenAuthenticationConfiguration)
			throws Exception {
		return anthenAuthenticationConfiguration.getAuthenticationManager();
	}

	@Bean // IoC가 됨.
	public BCryptPasswordEncoder encodePWD() {

		return new BCryptPasswordEncoder();
	}
	
	// 시큐리티가 대신 로그인해주는데 password를 가로채기를 하는데
	// 해당 password가 뭘로 해쉬가 되어 회원가입이 되었는지 알아야 
	// 같은 해쉬로 암호화해서 DB에 있는 해쉬랑 비교할 수 있음.

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// 1. csrf 비활성화 
		http.csrf(c-> c.disable());
		
		// 2. 인증 주소 설정 (WEB-INF/** 추가해줘야 함. 아니면 인증이 필요한 주소를 무한 리다이렉션 일어남)
		http.authorizeHttpRequests(
				auth -> auth.requestMatchers("/", "/auth/**", "/WEB-INF/**", "/js/**", "/css/**", "/image/**", "/dummy/**")
						.permitAll().anyRequest().authenticated());

		// 3. 로그인 처리 프로세스 설정 
		http.formLogin(f -> f.loginPage("/auth/loginForm")
				.loginProcessingUrl("/auth/loginProc")
				.defaultSuccessUrl("/"));

		return http.build();
	}

}
