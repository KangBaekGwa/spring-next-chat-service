package com.baekgwa.chatservice.global.response;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * PackageName : com.baekgwa.chatservice.global.response
 * FileName    : SuccessCode
 * Author      : Baekgwa
 * Date        : 2025-10-02
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-02     Baekgwa               Initial creation
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SuccessCode {

	// Authentication
	LOGIN_SUCCESS(HttpStatus.OK, "로그인 성공"),
	LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃 성공"),

	// User
	SIGNUP_SUCCESS(HttpStatus.CREATED, "회원가입 성공"),

	// Room
	CREATE_CHAT_ROOM_SUCCESS(HttpStatus.CREATED, "채팅방 생성 완료"),
	JOIN_CHAT_ROOM_SUCCESS(HttpStatus.CREATED, "채팅방 입장 성공"),
	JOINED_CHAT_ROOM_LIST_SUCCESS(HttpStatus.OK, "내가 가입한 채팅방 목록 조회 성공"),
	SEARCH_ROOM_SUCCESS(HttpStatus.OK, "채팅방 조회 성공"),

	// Chat
	GET_CHAT_MESSAGE_SUCCESS(HttpStatus.OK, "채팅방 메시지 조회 성공"),

	// Common
	REQUEST_SUCCESS(HttpStatus.OK, "요청 응답 성공.");

	private final HttpStatus status;
	private final String message;
}

