package com.baekgwa.chatservice.global.websocket.handler;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.baekgwa.chatservice.domain.chat.dto.ChatRequestDto;
import com.baekgwa.chatservice.domain.chat.dto.ChatResponseDto;
import com.baekgwa.chatservice.domain.chat.manager.ChatRoomManager;
import com.baekgwa.chatservice.domain.chat.service.ChatMessageSequenceService;
import com.baekgwa.chatservice.domain.chat.service.ChatService;
import com.baekgwa.chatservice.model.chat.member.repository.ChatRoomMemberRepository;
import com.baekgwa.chatservice.model.chat.message.entity.ChatMessageEntity;
import com.baekgwa.chatservice.model.chat.room.repository.ChatRoomRepository;
import com.baekgwa.chatservice.model.user.entity.UserEntity;
import com.baekgwa.chatservice.model.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * PackageName : com.baekgwa.chatservice.global.websocket.handler
 * FileName    : WebSocketChatHandler
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
public class WebSocketChatHandler implements WebSocketHandler {

	private final ObjectMapper objectMapper;
	private final ChatService chatService;
	private final ChatRoomManager chatRoomManager;
	private final ChatMessageSequenceService chatMessageSequenceService;
	private final UserRepository userRepository;

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.info("WebSocket connection established: Session ID {}", session.getId());
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		try {
			String payload = ((TextMessage) message).getPayload();
			ChatRequestDto requestDto = objectMapper.readValue(payload, ChatRequestDto.class);
			Long userId = (Long) session.getAttributes().get("userId");

			switch (requestDto.getType()) {
				case ENTER -> handleEnter(session, requestDto.getRoomId(), userId);
				case TALK -> handleTalk(userId, requestDto.getRoomId(), requestDto.getContent());
			}
		} catch (Exception e) {
			log.error("Error handling message from session {}: {}", session.getId(), e.getMessage(), e);
			session.sendMessage(new TextMessage("{\"error\":\"An unexpected error occurred.\"}"));
			session.close(CloseStatus.SERVER_ERROR);
		}
	}

	private void handleEnter(WebSocketSession session, Long roomId, Long userId) throws IOException {
		// 1. 사용자가 해당 채팅방의 멤버인지 권한 확인
		boolean isMember = chatService.isRoomMember(userId, roomId);

		if (!isMember) {
			// 권한이 없으면 에러 메시지 전송 후 연결 종료
			log.warn("Unauthorized attempt to enter room: userId={}, roomId={}", userId, roomId);
			session.sendMessage(new TextMessage("{\"error\":\"Authorization Failed\"}"));
			session.close(CloseStatus.POLICY_VIOLATION); // 정책 위반으로 세션 종료
			return;
		}

		// 2. 만약 사용자가 다른 방에 이미 들어가 있었다면, 이전 방에서 퇴장 처리
		Long oldRoomId = (Long) session.getAttributes().get("roomId");
		if (oldRoomId != null && !oldRoomId.equals(roomId)) {
			chatRoomManager.leaveRoom(oldRoomId, session);
			log.info("User {} moved from room {} to {}", userId, oldRoomId, roomId);
		}

		// 3. 권한이 확인된 경우에만 입장 처리
		chatRoomManager.enterRoom(roomId, session);
		session.getAttributes().put("roomId", roomId);

		log.info("User {} entered room {}", userId, roomId);
	}

	private void handleTalk(Long userId, Long roomId, String content) {
		long sequence = chatMessageSequenceService.getNextSequence(roomId);
		ChatMessageEntity savedMessage = chatService.sendMessage(userId, roomId, content, sequence);
		String username = userRepository.findById(userId)
			.map(UserEntity::getUsername)
			.orElse("Unknown User");
		ChatResponseDto responseDto = ChatResponseDto.from(savedMessage, username);
		chatRoomManager.sendMessageToRoom(roomId, responseDto);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		// 세션에 저장된 roomId를 통해 퇴장 처리
		Long roomId = (Long) session.getAttributes().get("roomId");
		if (roomId != null) {
			chatRoomManager.leaveRoom(roomId, session);
		}
		log.info("WebSocket connection closed: Session ID {}, Status: {}", session.getId(), status);
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		log.error("Transport error on session {}: {}", session.getId(), exception.getMessage());
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}
}
