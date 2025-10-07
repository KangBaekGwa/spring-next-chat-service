package com.baekgwa.chatservice.global.websocket.interceptor;

import static com.baekgwa.chatservice.global.security.constant.JwtConstant.*;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.baekgwa.chatservice.global.response.RequestUtil;
import com.baekgwa.chatservice.global.security.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * PackageName : com.baekgwa.chatservice.global.websocket.interceptor
 * FileName    : WebSocketHandshakeInterceptor
 * Author      : Baekgwa
 * Date        : 2025-10-04
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-04     Baekgwa               Initial creation
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

	private final JwtUtil jwtUtil;

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
		Map<String, Object> attributes) throws Exception {

		// 1. Security 에 의해 인증된 사용자 정보 조회
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			log.warn("Handshake rejected: User not authenticated. URI: {}", request.getURI());
			return false;
		}

		Long userId = (Long)authentication.getPrincipal();

		// 2. Token 만료기간 기준으로, 해당 Session 의 만료기간 설정
		if (!(request instanceof ServletServerHttpRequest servletRequest)) {
			log.warn("Handshake rejected: Request is not a ServletServerHttpRequest. URI: {}", request.getURI());
			return false; // 서블릿 기반의 요청이 아니면 처리 불가
		}

		Optional<String> accessTokenOptional = RequestUtil.getCookieValue(
			servletRequest.getServletRequest(),
			ACCESS_TOKEN_COOKIE_NAME
		);

		if (accessTokenOptional.isEmpty()) {
			log.warn("Handshake rejected: Missing access token in cookie. URI: {}", request.getURI());
			return false;
		}
		String accessToken = accessTokenOptional.get();
		Date expirationDate = jwtUtil.getExpirationDate(accessToken);

		// 3. 토큰 정보를 세션 속성에 저장.
		attributes.put("userId", userId);
		attributes.put("expirationDate", expirationDate);

		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
		Exception exception) {
		if (exception != null) {
			log.error("Handshake failed with exception. URI: {}, Exception: {}",
				request.getURI(), exception.getMessage());
		}
	}
}
