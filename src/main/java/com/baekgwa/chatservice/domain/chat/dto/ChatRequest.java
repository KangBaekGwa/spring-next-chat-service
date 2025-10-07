package com.baekgwa.chatservice.domain.chat.dto;

import com.baekgwa.chatservice.domain.chat.type.MessageType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * PackageName : com.baekgwa.chatservice.domain.chat.dto
 * FileName    : ChatRequest
 * Author      : Baekgwa
 * Date        : 2025-10-04
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-04     Baekgwa               Initial creation
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatRequest {

	@Getter
	@NoArgsConstructor
	public static class ChatMessageRequest {
		private MessageType type;
		private Long roomId;
		private String content;
	}
}
