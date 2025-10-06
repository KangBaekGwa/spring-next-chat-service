package com.baekgwa.chatservice.domain.user.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baekgwa.chatservice.domain.user.dto.UserRequest;
import com.baekgwa.chatservice.domain.user.service.UserService;
import com.baekgwa.chatservice.global.response.BaseResponse;
import com.baekgwa.chatservice.global.response.SuccessCode;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * PackageName : com.baekgwa.chatservice.domain.user.controller
 * FileName    : UserController
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
@RequestMapping("/user")
@Tag(name = "User Controller", description = "회원 도메인")
public class UserController {

	private final UserService userService;

	@PostMapping("/signup")
	@Operation(summary = "회원 가입")
	public BaseResponse<Void> login(
		@RequestBody @Valid UserRequest.SignupDto signupDto
	) {
		userService.signup(signupDto);
		return BaseResponse.success(SuccessCode.SIGNUP_SUCCESS);
	}
}
