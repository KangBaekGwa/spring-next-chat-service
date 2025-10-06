package com.baekgwa.chatservice.domain.room.dto;

import com.baekgwa.chatservice.model.chat.member.entity.ChatRoomMemberEntity;
import com.baekgwa.chatservice.model.chat.room.entity.ChatRoomEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * PackageName : com.baekgwa.chatservice.domain.room.dto
 * FileName    : RoomResponse
 * Author      : Baekgwa
 * Date        : 2025-10-05
 * Description :
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-05     Baekgwa               Initial creation
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RoomResponse {

	@Getter
	@Builder(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class CreateChatRoomResponse {
		private final Long chatRoomId;
		private final String title;

		public static CreateChatRoomResponse from(ChatRoomEntity chatRoom) {
			return CreateChatRoomResponse
				.builder()
				.chatRoomId(chatRoom.getId())
				.title(chatRoom.getTitle())
				.build();
		}
	}

	@Getter
	@Builder(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class JoinedRoomResponse {
		private final Long roomId;
		private final String title;

		public static JoinedRoomResponse of(ChatRoomMemberEntity chatRoomMember) {
			return JoinedRoomResponse
				.builder()
				.roomId(chatRoomMember.getChatRoom().getId())
				.title(chatRoomMember.getChatRoom().getTitle())
				.build();
		}
	}

	@Getter
	@Builder(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class ChatRoomResponse {
		private final Long roomId;
		private final String title;

		public static ChatRoomResponse from(ChatRoomEntity chatRoom) {
			return ChatRoomResponse
				.builder()
				.roomId(chatRoom.getId())
				.title(chatRoom.getTitle())
				.build();
		}
	}
}
