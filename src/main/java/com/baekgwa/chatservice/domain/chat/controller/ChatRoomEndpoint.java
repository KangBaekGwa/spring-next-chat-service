package com.baekgwa.chatservice.domain.chat.controller;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import com.baekgwa.chatservice.domain.chat.manager.ChatRoomManager;

import lombok.RequiredArgsConstructor;

/**
 * PackageName : com.baekgwa.chatservice.domain.chat.controller
 * FileName    : ChatRoomEndpoint
 * Author      : Baekgwa
 * Date        : 2025-10-07
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-07     Baekgwa               Initial creation
 */
@Component
@Endpoint(id = "chat-rooms") // 엔드포인트 URL 경로가 됩니다. (/actuator/chat-rooms)
@RequiredArgsConstructor
public class ChatRoomEndpoint {

	private final ChatRoomManager chatRoomManager;

	@ReadOperation
	public Map<String, Object> getChatRoomStatus() {
		return chatRoomManager.getRoomSessions().entrySet().stream()
			.collect(Collectors.toMap(
				entry -> "Room-" + entry.getKey(), // Key: "Room-123"
				entry -> entry.getValue().stream()
					.map(session -> Map.of(
						"sessionId", session.getId(),
						"attributes", session.getAttributes()
					))
					.toList()
			));
	}
}