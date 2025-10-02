package com.baekgwa.chatservice.global.config;

import static org.springframework.http.HttpMethod.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import com.baekgwa.chatservice.global.security.enrypoint.CustomAccessDeniedHandler;
import com.baekgwa.chatservice.global.security.enrypoint.CustomAuthenticationEntryPoint;
import com.baekgwa.chatservice.global.security.filter.AuthenticationFilter;

import lombok.RequiredArgsConstructor;

/**
 * PackageName : com.baekgwa.chatservice.global.config
 * FileName    : SecurityConfig
 * Author      : Baekgwa
 * Date        : 2025-10-02
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-02     Baekgwa               Initial creation
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final CorsConfigurationSource corsConfigurationSource;
	private final AuthenticationFilter authenticationFilter;
	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	private final CustomAccessDeniedHandler customAccessDeniedHandler;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			// ✅ 보안 관련 설정 (CSRF, CORS, 세션)
			.csrf(AbstractHttpConfigurer::disable)

			// ✅ 기본 인증 방식 비활성화 (JWT 사용)
			.httpBasic(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

			// ✅ Cors Setting
			.cors(cors -> cors.configurationSource(corsConfigurationSource))

			// ✅ End-point Setting
			.authorizeHttpRequests(authorize -> authorize
				// 프론트엔드에서 적용될 예외 포인트 설정
				.requestMatchers("/error", "/favicon.ico").permitAll()
				// Swagger 문서 접근 허용
				.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**",
					"/swagger-ui.html").permitAll()

				// authentication
				.requestMatchers(POST, "/auth/login").permitAll()
				.requestMatchers(POST, "/auth/logout").permitAll()

				// user
				.requestMatchers(POST, "/user/signup").permitAll()

				.anyRequest().authenticated());

		// ❗ 인증 Filter 추가
		http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

		// ❗ AuthenticationEntryPoint Custom Handler
		http.exceptionHandling(exception -> exception
			.authenticationEntryPoint(customAuthenticationEntryPoint)
			.accessDeniedHandler(customAccessDeniedHandler));

		return http.build();
	}
}
