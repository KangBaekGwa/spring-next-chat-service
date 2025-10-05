package com.baekgwa.chatservice.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.baekgwa.chatservice.global.websocket.handler.WebSocketChatHandler;
import com.baekgwa.chatservice.global.websocket.interceptor.WebSocketHandshakeInterceptor;

import lombok.RequiredArgsConstructor;

/**
 * PackageName : com.baekgwa.chatservice.global.config
 * FileName    : WebSocketConfig
 * Author      : Baekgwa
 * Date        : 2025-10-04
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-04     Baekgwa               Initial creation
 */
@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

	private final WebSocketHandshakeInterceptor webSocketHandshakeInterceptor;
	private final WebSocketChatHandler webSocketChatHandler;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry
			.addHandler(webSocketChatHandler, "/ws/chat")
			.addInterceptors(webSocketHandshakeInterceptor)
			.setAllowedOrigins("*");
	}
}
