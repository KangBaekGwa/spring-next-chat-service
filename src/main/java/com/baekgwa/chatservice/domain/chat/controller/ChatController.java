package com.baekgwa.chatservice.domain.chat.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baekgwa.chatservice.domain.chat.dto.ChatResponse;
import com.baekgwa.chatservice.domain.chat.service.ChatService;
import com.baekgwa.chatservice.domain.chat.type.MessageDirection;
import com.baekgwa.chatservice.global.response.BaseResponse;
import com.baekgwa.chatservice.global.response.CursorResponse;
import com.baekgwa.chatservice.global.response.SuccessCode;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * PackageName : com.baekgwa.chatservice.domain.chat.controller
 * FileName    : ChatController
 * Author      : Baekgwa
 * Date        : 2025-10-05
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-05     Baekgwa               Initial creation
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
@Tag(name = "Chat Controller", description = "채팅 도메인")
public class ChatController {

	private final ChatService chatService;

	@GetMapping("/room/{roomId}")
	@Operation(summary = "채팅방 메시지 조회")
	public BaseResponse<CursorResponse<ChatResponse.ChatMessageDetail>> getChatMessage(
		@PathVariable("roomId") Long roomId,
		@RequestParam(value = "sequence", required = false) Long sequence,
		@RequestParam(value = "size", required = false, defaultValue = "50") Integer size,
		@RequestParam(value = "direction", required = false, defaultValue = "BEFORE") MessageDirection direction,
		@AuthenticationPrincipal Long userId
	) {
		CursorResponse<ChatResponse.ChatMessageDetail> response =
			chatService.getChatMessage(userId, roomId, sequence, size, direction);

		return BaseResponse.success(SuccessCode.GET_CHAT_MESSAGE_SUCCESS, response);
	}
}
