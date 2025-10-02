package com.baekgwa.chatservice.domain.authentication.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * PackageName : com.baekgwa.chatservice.domain.authentication.dto
 * FileName    : AuthResponse
 * Author      : Baekgwa
 * Date        : 2025-10-02
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-02     Baekgwa               Initial creation
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthResponse {

	@Getter
	public static class LoginResponse {
		private final String accessToken;

		private LoginResponse(String accessToken) {
			this.accessToken = accessToken;
		}

		public static LoginResponse from(String accessToken) {
			return new LoginResponse(accessToken);
		}
	}
}
