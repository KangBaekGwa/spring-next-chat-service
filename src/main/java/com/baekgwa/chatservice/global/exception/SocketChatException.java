package com.baekgwa.chatservice.global.exception;

import lombok.Getter;

/**
 * PackageName : com.baekgwa.chatservice.global.exception
 * FileName    : SocketChatException
 * Author      : Baekgwa
 * Date        : 2025-10-07
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-07     Baekgwa               Initial creation
 */
@Getter
public class SocketChatException extends RuntimeException {

	private final String errorMessage;

	public SocketChatException(final String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}

	// 새로 추가한 생성자 ✅
	public SocketChatException(final String format, final Object... args) {
		super(String.format(format, args));
		this.errorMessage = String.format(format, args);
	}
}

