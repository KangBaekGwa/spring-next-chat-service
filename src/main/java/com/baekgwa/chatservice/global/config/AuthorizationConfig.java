package com.baekgwa.chatservice.global.config;

import static org.springframework.http.HttpMethod.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;

/**
 * PackageName : com.baekgwa.chatservice.global.config
 * FileName    : AuthorizationConfig
 * Author      : Baekgwa
 * Date        : 2025-10-04
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-04     Baekgwa               Initial creation
 */
@Configuration
public class AuthorizationConfig {

	@Bean
	public Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> authorizeRequestsCustomizer() {
		return authorize -> authorize
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

			.anyRequest().authenticated();
	}
}