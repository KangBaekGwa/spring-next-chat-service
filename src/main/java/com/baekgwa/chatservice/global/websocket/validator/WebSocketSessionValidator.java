package com.baekgwa.chatservice.global.websocket.validator;

import static com.baekgwa.chatservice.global.websocket.constant.WebSocketConstant.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.baekgwa.chatservice.domain.chat.dto.ChatResponse;
import com.baekgwa.chatservice.domain.chat.manager.ChatRoomManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * PackageName : com.baekgwa.chatservice.global.websocket.validator
 * FileName    : WebSocketSessionValidator
 * Author      : Baekgwa
 * Date        : 2025-10-07
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-07     Baekgwa               Initial creation
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketSessionValidator {

	private final ChatRoomManager chatRoomManager;
	private final ObjectMapper objectMapper;

	// 5분마다 실행 (300000 밀리초)
	@Scheduled(fixedRateString = "300000")
	public void validateSessions() {
		log.info("채팅방 연결 세션, 유효성 검증 체크 진행 중... {}", LocalDateTime.now());
		final Date now = new Date();

		Map<Long, Set<WebSocketSession>> allRoomSessions = chatRoomManager.getRoomSessions();

		allRoomSessions.values().stream()
			.flatMap(Set::stream) // 모든 방의 세션들을 하나의 스트림으로 만듦
			.distinct()
			.forEach(session -> {
				Date expirationDate = (Date)session.getAttributes().get(SESSION_ATTRIBUTE_KEY_EXPIRED_DATE);

				if (expirationDate != null && expirationDate.before(now)) {
					log.warn("세션 [sessionId : {}] expired. 연결을 해제 합니다.", session.getId());
					try {
						ChatResponse.ChatErrorMessage message
							= ChatResponse.ChatErrorMessage.of("로그인 인증 기간이 지났습니다. 다시 로그인 해주세요.");
						TextMessage errorMessage = new TextMessage(objectMapper.writeValueAsString(message));
						session.sendMessage(errorMessage);
						session.close(CloseStatus.POLICY_VIOLATION.withReason("Session expired"));
					} catch (JsonProcessingException e) {
						log.error(
							"CRITICAL: Failed to serialize ChatErrorMessage for session {}. This is a server bug.",
							session.getId(), e);
					} catch (IOException e) {
						log.error("Error closing expired session {}: {}", session.getId(), e.getMessage());
					}
				}
			});
	}
}
