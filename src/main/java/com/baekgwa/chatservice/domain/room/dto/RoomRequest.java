package com.baekgwa.chatservice.domain.room.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * PackageName : com.baekgwa.chatservice.domain.room.dto
 * FileName    : RoomRequest
 * Author      : Baekgwa
 * Date        : 2025-10-05
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-05     Baekgwa               Initial creation
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RoomRequest {

	@Getter
	@NoArgsConstructor
	public static class CreateChatRoomRequest {
		private String title;
		private String password;
	}

	@Getter
	@NoArgsConstructor
	public static class JoinChatRoomRequest {
		private String password;
	}
}
