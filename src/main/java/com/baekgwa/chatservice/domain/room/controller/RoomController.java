package com.baekgwa.chatservice.domain.room.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baekgwa.chatservice.domain.room.dto.RoomRequest;
import com.baekgwa.chatservice.domain.room.dto.RoomResponse;
import com.baekgwa.chatservice.domain.room.service.RoomService;
import com.baekgwa.chatservice.domain.room.type.RoomSortType;
import com.baekgwa.chatservice.global.response.BaseResponse;
import com.baekgwa.chatservice.global.response.PageResponse;
import com.baekgwa.chatservice.global.response.SuccessCode;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * PackageName : com.baekgwa.chatservice.domain.room.controller
 * FileName    : RoomController
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
@RequestMapping("/room")
@Tag(name = "Room Controller", description = "채팅방 도메인")
public class RoomController {

	private final RoomService roomService;

	@PostMapping
	@Operation(summary = "채팅방 생성")
	public BaseResponse<RoomResponse.CreateChatRoomResponse> createNewChatRoom(
		@Valid @RequestBody RoomRequest.CreateChatRoomRequest request,
		@AuthenticationPrincipal Principal principal
	) {
		Long userId = Long.parseLong(principal.getName());
		RoomResponse.CreateChatRoomResponse response
			= roomService.createChatRoom(request, userId);

		return BaseResponse.success(SuccessCode.CREATE_CHAT_ROOM_SUCCESS, response);
	}

	@PostMapping("/{roomId}/join")
	@Operation(summary = "특정 채팅방 참여")
	public BaseResponse<Void> joinChatRoom(
		@PathVariable("roomId") Long roomId,
		@Valid @RequestBody RoomRequest.JoinChatRoomRequest request,
		@AuthenticationPrincipal Principal principal
	) {
		Long userId = Long.parseLong(principal.getName());
		roomService.joinChatRoom(roomId, request, userId);

		return BaseResponse.success(SuccessCode.JOIN_CHAT_ROOM_SUCCESS);
	}

	@GetMapping("/joined")
	@Operation(summary = "참여한 채팅방 목록 조회")
	public BaseResponse<List<RoomResponse.JoinedRoomResponse>> getJoinedChatRoomList(
		@AuthenticationPrincipal Principal principal
	) {
		Long userId = Long.parseLong(principal.getName());
		List<RoomResponse.JoinedRoomResponse> response = roomService.getJoinedChatRoomList(userId);

		return BaseResponse.success(SuccessCode.JOINED_CHAT_ROOM_LIST_SUCCESS, response);
	}

	@GetMapping
	@Operation(summary = "채팅방 키워드 조회")
	public BaseResponse<PageResponse<RoomResponse.ChatRoomResponse>> searchRoomList(
		@RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
		@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
		@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
		@RequestParam(value = "sort", required = false, defaultValue = "LATEST") RoomSortType sort
	) {
		PageResponse<RoomResponse.ChatRoomResponse> response = roomService.searchRoomList(size, page, keyword, sort);
		return BaseResponse.success(SuccessCode.SEARCH_ROOM_SUCCESS, response);
	}
}
