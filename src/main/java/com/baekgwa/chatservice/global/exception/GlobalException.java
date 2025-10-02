package com.baekgwa.chatservice.global.exception;

import com.baekgwa.chatservice.global.response.ErrorCode;

import lombok.Getter;

/**
 * PackageName : com.baekgwa.chatservice.global.exception
 * FileName    : GlobalException
 * Author      : Baekgwa
 * Date        : 2025-10-02
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-02     Baekgwa               Initial creation
 */
@Getter
public class GlobalException extends RuntimeException {

	private final ErrorCode errorCode;

	public GlobalException(final ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
