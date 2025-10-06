package com.baekgwa.chatservice.domain.chat.dto;

import java.time.LocalDateTime;

import com.baekgwa.chatservice.domain.chat.type.MessageType;
import com.baekgwa.chatservice.model.chat.message.entity.ChatMessageEntity;
import com.baekgwa.chatservice.model.chat.room.entity.ChatRoomEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * PackageName : com.baekgwa.chatservice.domain.chat.dto
 * FileName    : ChatResponseDto
 * Author      : Baekgwa
 * Date        : 2025-10-04
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-04     Baekgwa               Initial creation
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatResponseDto {

	@Getter
	@Builder(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class ChatMessageResponse {
		private MessageType type;
		private Long roomId;
		private Long senderId;
		private String senderUsername;
		private String content;
		private Long sequence;
		private LocalDateTime sentAt;

		public static ChatMessageResponse from(ChatMessageEntity message, String username) {
			return ChatMessageResponse.builder()
				.type(MessageType.TALK)
				.roomId(message.getChatRoom().getId())
				.senderId(message.getSender().getId())
				.senderUsername(username)
				.content(message.getContent())
				.sequence(message.getSequence())
				.sentAt(message.getCreatedAt())
				.build();
		}
	}
}
