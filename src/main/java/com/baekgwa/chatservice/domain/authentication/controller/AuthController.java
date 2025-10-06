package com.baekgwa.chatservice.domain.authentication.controller;

import static com.baekgwa.chatservice.global.security.constant.JwtConstant.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baekgwa.chatservice.domain.authentication.dto.AuthRequest;
import com.baekgwa.chatservice.domain.authentication.dto.AuthResponse;
import com.baekgwa.chatservice.domain.authentication.service.AuthService;
import com.baekgwa.chatservice.global.environment.JwtProperties;
import com.baekgwa.chatservice.global.response.BaseResponse;
import com.baekgwa.chatservice.global.response.ResponseUtil;
import com.baekgwa.chatservice.global.response.SuccessCode;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * PackageName : com.baekgwa.chatservice.domain.authentication.controller
 * FileName    : AuthController
 * Author      : Baekgwa
 * Date        : 2025-10-02
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-02     Baekgwa               Initial creation
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Auth Controller", description = "인증 도메인")
public class AuthController {

	private final AuthService authService;
	private final JwtProperties jwtProperties;

	@PostMapping("/login")
	@Operation(summary = "로그인")
	public BaseResponse<Void> login(
		@Valid @RequestBody AuthRequest.LoginDto loginDto,
		HttpServletResponse response
	) {
		AuthResponse.LoginResponse loginResponse = authService.login(loginDto);
		ResponseUtil.addCookie(
			response,
			ACCESS_TOKEN_COOKIE_NAME,
			loginResponse.getAccessToken(),
			jwtProperties.getTokenExpirationMin().intValue() * 60);
		return BaseResponse.success(SuccessCode.LOGIN_SUCCESS);
	}

	@PostMapping("/logout")
	@Operation(summary = "로그아웃")
	public BaseResponse<Void> logout(HttpServletResponse response) {
		ResponseUtil.removeCookie(response, ACCESS_TOKEN_COOKIE_NAME);
		return BaseResponse.success(SuccessCode.LOGOUT_SUCCESS);
	}
}
