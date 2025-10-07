package com.baekgwa.chatservice.global.websocket.handler;

import static com.baekgwa.chatservice.global.websocket.constant.WebSocketConstant.*;

import java.io.IOException;

import org.slf4j.event.Level;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.baekgwa.chatservice.domain.chat.dto.ChatRequest;
import com.baekgwa.chatservice.domain.chat.dto.ChatResponse;
import com.baekgwa.chatservice.domain.chat.manager.ChatRoomManager;
import com.baekgwa.chatservice.domain.chat.service.ChatMessageSequenceService;
import com.baekgwa.chatservice.domain.chat.service.ChatService;
import com.baekgwa.chatservice.global.exception.SocketChatException;
import com.baekgwa.chatservice.model.chat.message.entity.ChatMessageEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
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

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.debug("웹 소켓 연결 완료: Session ID {}", session.getId());
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		try {
			String payload = ((TextMessage)message).getPayload();
			ChatRequest.ChatMessageRequest requestDto
				= objectMapper.readValue(payload, ChatRequest.ChatMessageRequest.class);
			Long userId = (Long)session.getAttributes().get(SESSION_ATTRIBUTE_KEY_USER_ID);

			switch (requestDto.getType()) {
				case ENTER -> handleEnter(session, requestDto.getRoomId(), userId);
				case TALK -> handleTalk(session, userId, requestDto.getRoomId(), requestDto.getContent());
			}
		} catch (JsonProcessingException e) {
			sendErrorMessage(session, "메시지 형식이 올바르지 않습니다. : " + e.getMessage(), Level.WARN);
		} catch (SocketChatException e) { //비지니스 오류
			sendErrorMessage(session, e.getErrorMessage(), Level.INFO);
		} catch (IOException e) {
			sendErrorMessage(session, "네트워크 오류 입니다. 다시 시도해 주세요.", Level.WARN);
		}
	}

	private void sendErrorMessage(WebSocketSession session, String errorMessage, Level logLevel) throws IOException {
		// 1. 로그 레벨에 따라 로깅 처리
		switch (logLevel) {
			case WARN -> log.warn("session {}: {}", session.getId(), errorMessage);
			case ERROR -> log.error("session {}: {}", session.getId(), errorMessage);
			case INFO -> log.info("session {}: {}", session.getId(), errorMessage);
			case DEBUG -> log.debug("session {}: {}", session.getId(), errorMessage);
			case TRACE -> log.trace("session {}: {}", session.getId(), errorMessage);
		}

		// 2. Error Message 전달
		try {
			ChatResponse.ChatErrorMessage message = ChatResponse.ChatErrorMessage.of(errorMessage);
			session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
		} catch (JsonProcessingException e) {
			log.error("CRITICAL: Failed to serialize ChatErrorMessage DTO. Sending failsafe message.", e);
			String failsafeJson = "{\"type\":\"ERROR\",\"content\":\"서버 내부 오류로 인해 응답을 생성할 수 없습니다.\"}";
			session.sendMessage(new TextMessage(failsafeJson));
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		chatRoomManager.leaveAllRooms(session);
		log.debug("웹 소켓 연결 해제: Session ID {}, Status: {}", session.getId(), status);
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		log.error("Transport error on session {}: {}", session.getId(), exception.getMessage());
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

	private void handleEnter(WebSocketSession session, Long roomId, Long userId) throws IOException {
		// 1. 사용자가 해당 채팅방의 멤버인지 권한 확인
		boolean isMember = chatService.isRoomMember(userId, roomId);

		if (!isMember) {
			throw new SocketChatException("roomId={} 방의 회원이 아닙니다!", roomId);
		}

		// 2. 만약 사용자가 다른 방에 이미 들어가 있었다면, 이전 방에서 퇴장 처리
		Long oldRoomId = (Long)session.getAttributes().get(SESSION_ATTRIBUTE_KEY_ROOM_ID);
		if (oldRoomId != null && !oldRoomId.equals(roomId)) {
			chatRoomManager.leaveRoom(oldRoomId, session);
			log.debug("회원 [userId : {}] 기존 방 [oldRoomId : {}] 에서 나갔습니다.", userId, oldRoomId);
		}

		// 3. 권한이 확인된 경우에만 입장 처리
		chatRoomManager.enterRoom(roomId, session);
		session.getAttributes().put(SESSION_ATTRIBUTE_KEY_ROOM_ID, roomId);

		log.debug("[Session : {}] 회원 [userId : {}] 가 방 [roomId : {}]에 접속하였습니다.", session.getId(), userId, roomId);
	}

	private void handleTalk(WebSocketSession session, Long userId, Long roomId, String content) throws JsonProcessingException {
		// 1. 메시지 순서용 sequence 계산
		long sequence = chatMessageSequenceService.getNextSequence(roomId);

		// 2. 신규 메시지 저장
		ChatMessageEntity savedMessage =
			chatService.sendMessage(userId, roomId, content, sequence);

		// 3. 신규 메시지, 연결된 회원에게 브로드 캐스팅
		ChatResponse.ChatMessageDetail newMessage = ChatResponse.ChatMessageDetail.of(savedMessage);
		chatRoomManager.sendMessageToRoom(roomId, newMessage, session.getId());
	}
}
