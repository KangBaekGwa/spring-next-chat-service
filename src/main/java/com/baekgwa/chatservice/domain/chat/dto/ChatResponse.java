package com.baekgwa.chatservice.domain.chat.dto;

import java.time.LocalDateTime;

import com.baekgwa.chatservice.model.chat.message.entity.ChatMessageEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * PackageName : com.baekgwa.chatservice.domain.chat.dto
 * FileName    : ChatResponse
 * Author      : Baekgwa
 * Date        : 2025-10-04
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-04     Baekgwa               Initial creation
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatResponse {
	@Getter
	@Builder(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class ChatMessageDetail {
		private final Long senderId;
		private final String sender;
		private final String content;
		private final Long sequence;
		private final LocalDateTime sendAt;

		public static ChatMessageDetail of(ChatMessageEntity message) {
			return ChatMessageDetail
				.builder()
				.senderId(message.getSender().getId())
				.sender(message.getSender().getUsername())
				.content(message.getContent())
				.sequence(message.getSequence())
				.sendAt(message.getCreatedAt())
				.build();
		}
	}

	@Getter
	public static class ChatErrorMessage {
		private final String error;

		public ChatErrorMessage(String error) {
			this.error = error;
		}

		public static ChatErrorMessage of(String error) {
			return new ChatErrorMessage(error);
		}
	}
}
