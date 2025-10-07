package com.baekgwa.chatservice.domain.chat.manager;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.baekgwa.chatservice.global.exception.SocketChatException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * PackageName : com.baekgwa.chatservice.domain.chat.manager
 * FileName    : ChatRoomManager
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
public class ChatRoomManager {

	private final ObjectMapper objectMapper;
	@Getter
	private final Map<Long, Set<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();

	/**
	 * 특정 채팅방에 사용자가 입장합니다.
	 * @param roomId 채팅방 ID
	 * @param session 사용자의 WebSocket 세션
	 */
	public void enterRoom(Long roomId, WebSocketSession session) {
		// roomId에 해당하는 세션 집합을 가져오거나, 없으면 새로 생성하여 추가
		roomSessions.computeIfAbsent(roomId, key -> new CopyOnWriteArraySet<>()).add(session);
	}

	/**
	 * 사용자가 채팅방에서 퇴장합니다. (연결 종료 시)
	 * @param roomId 채팅방 ID
	 * @param session 사용자의 WebSocket 세션
	 */
	public void leaveRoom(Long roomId, WebSocketSession session) {
		Set<WebSocketSession> sessions = roomSessions.get(roomId);
		if (sessions != null) {
			sessions.remove(session);
			// 만약 방에 아무도 없으면, 맵에서 해당 방 자체를 제거
			if (sessions.isEmpty()) {
				roomSessions.remove(roomId);
				log.info("Room {} is now empty and removed.", roomId);
			}
		}
	}

	/**
	 * 특정 세션이 모든 채팅방에서 퇴장하도록 처리합니다.
	 * 연결이 끊어졌을 때 호출되어 해당 세션과 관련된 모든 정보를 정리합니다.
	 * @param session 정리할 WebSocket 세션
	 */
	public void leaveAllRooms(WebSocketSession session) {
		final String sessionId = session.getId();

		roomSessions.forEach((roomId, sessions) -> {
			boolean removed = sessions.remove(session);

			// 만약 세션이 성공적으로 제거되었다면, 로그를 남깁니다.
			if (removed) {
				log.info("Session [{}] removed from room [{}].", sessionId, roomId);
				// 세션 제거 후 해당 방이 비어있게 되었다면, 맵에서 방 자체를 제거하여 메모리 누수를 방지합니다.
				if (sessions.isEmpty()) {
					roomSessions.remove(roomId);
					log.info("Room [{}] is now empty and has been removed.", roomId);
				}
			}
		});
	}

	/**
	 * 특정 채팅방의 모든 사용자에게 메시지를 보냅니다. (브로드캐스팅)
	 * @param roomId 메시지를 보낼 채팅방 ID
	 * @param message 전송할 메시지 객체 (DTO)
	 */
	public <T> void sendMessageToRoom(Long roomId, T message, String senderId) throws JsonProcessingException {
		Set<WebSocketSession> sessions = roomSessions.get(roomId);

		if (sessions == null) {
			throw new SocketChatException("No active sessions in room {} to send message.", roomId);
		}

		// DTO 객체를 JSON 문자열로 변환
		String jsonMessage = objectMapper.writeValueAsString(message);
		TextMessage textMessage = new TextMessage(jsonMessage);

		// 해당 방의 모든 세션에게 메시지 동시 전송 (병렬 처리)
		sessions.parallelStream()
			.filter(session -> !session.getId().equals(senderId))
			.forEach(session -> {
				if (session.isOpen()) {
					try {
						session.sendMessage(textMessage);
					} catch (IOException e) {
						log.error("Failed to send message to session {}: {}", session.getId(), e.getMessage());
					}
				}
			});
	}
}
