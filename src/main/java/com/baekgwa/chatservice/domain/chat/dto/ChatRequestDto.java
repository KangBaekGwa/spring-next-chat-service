package com.baekgwa.chatservice.domain.chat.dto;

import com.baekgwa.chatservice.domain.chat.type.MessageType;

import lombok.Getter;

/**
 * PackageName : com.baekgwa.chatservice.domain.chat.dto
 * FileName    : ChatRequestDto
 * Author      : Baekgwa
 * Date        : 2025-10-04
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-04     Baekgwa               Initial creation
 */
@Getter
public class ChatRequestDto {
	private MessageType type;
	private Long roomId;
	private String content;
}
