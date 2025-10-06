package com.baekgwa.chatservice.global.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * PackageName : com.baekgwa.chatservice.global.constant
 * FileName    : MessageConstant
 * Author      : Baekgwa
 * Date        : 2025-10-06
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-06     Baekgwa               Initial creation
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageConstant {

	// Redis Sequence Key
	public static final String SEQUENCE_KEY_PREFIX = "room:message:sequence";
	public static final String LOCK_KEY_PREFIX = "lock:sequence:";
}
