package com.baekgwa.chatservice.domain.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baekgwa.chatservice.domain.user.dto.UserRequest;
import com.baekgwa.chatservice.global.exception.GlobalException;
import com.baekgwa.chatservice.global.response.ErrorCode;
import com.baekgwa.chatservice.model.user.entity.UserEntity;
import com.baekgwa.chatservice.model.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * PackageName : com.baekgwa.chatservice.domain.user.service
 * FileName    : UserService
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
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public void signup(UserRequest.SignupDto signupDto) {

		// 1. 중복 검증
		if(userRepository.findByLoginId(signupDto.getLoginId()).isPresent()) {
			throw new GlobalException(ErrorCode.DUPLICATE_LOGIN_ID);
		}

		// 2. 새로운 회원 데이터 생성
		UserEntity newUser = UserEntity.of(
			signupDto.getLoginId(),
			passwordEncoder.encode(signupDto.getPassword()),
			signupDto.getUsername()
		);

		// 3. 데이터 저장
		userRepository.save(newUser);
	}
}
