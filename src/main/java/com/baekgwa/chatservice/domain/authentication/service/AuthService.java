package com.baekgwa.chatservice.domain.authentication.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baekgwa.chatservice.domain.authentication.dto.AuthRequest;
import com.baekgwa.chatservice.domain.authentication.dto.AuthResponse;
import com.baekgwa.chatservice.global.exception.GlobalException;
import com.baekgwa.chatservice.global.response.ErrorCode;
import com.baekgwa.chatservice.global.security.jwt.JwtUtil;
import com.baekgwa.chatservice.model.user.entity.UserEntity;
import com.baekgwa.chatservice.model.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * PackageName : com.baekgwa.chatservice.domain.authentication.service
 * FileName    : AuthService
 * Author      : Baekgwa
 * Date        : 2025-10-02
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-02     Baekgwa               Initial creation
 */
@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	@Transactional(readOnly = true)
	public AuthResponse.LoginResponse login(AuthRequest.LoginDto loginDto) {
		// 1. 사용자 정보 조회
		UserEntity findUser = userRepository.findByLoginId(loginDto.getLoginId()).orElseThrow(
			() -> new GlobalException(ErrorCode.INVALID_LOGIN_INFO));

		// 2. 패스워드 검증
		if (!passwordEncoder.matches(loginDto.getPassword(), findUser.getPassword())) {
			throw new GlobalException(ErrorCode.INVALID_LOGIN_INFO);
		}

		// 3. 회원 토큰 생성
		String accessToken = jwtUtil.createJwt(findUser.getId());

		// 4. 반환
		return AuthResponse.LoginResponse.from(accessToken);
	}
}
