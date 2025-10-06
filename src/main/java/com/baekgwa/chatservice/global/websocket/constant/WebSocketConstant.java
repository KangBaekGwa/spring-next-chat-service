package com.baekgwa.chatservice.global.websocket.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * PackageName : com.baekgwa.chatservice.global.websocket.constant
 * FileName    : WebSocketConstant
 * Author      : Baekgwa
 * Date        : 2025-10-06
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-06     Baekgwa               Initial creation
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WebSocketConstant {
	// 세션 attribute 에 등록될 key
	public static final String SESSION_ATTRIBUTE_KEY_USER_ID = "userId";
	public static final String SESSION_ATTRIBUTE_KEY_ROOM_ID = "roomId";
}
