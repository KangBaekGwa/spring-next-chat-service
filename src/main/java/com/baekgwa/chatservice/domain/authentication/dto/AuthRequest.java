package com.baekgwa.chatservice.domain.authentication.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * PackageName : com.baekgwa.chatservice.domain.authentication.dto
 * FileName    : AuthRequest
 * Author      : Baekgwa
 * Date        : 2025-10-02
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-02     Baekgwa               Initial creation
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthRequest {

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class LoginDto {
		@NotBlank(message = "로그인 아이디는 필수 입니다.")
		private String loginId;
		@NotBlank(message = "비밀번호는 필수 입니다.")
		private String password;
	}
}

