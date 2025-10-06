package com.baekgwa.chatservice.domain.room.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baekgwa.chatservice.domain.room.dto.RoomRequest;
import com.baekgwa.chatservice.domain.room.dto.RoomResponse;
import com.baekgwa.chatservice.domain.room.type.RoomSortType;
import com.baekgwa.chatservice.global.exception.GlobalException;
import com.baekgwa.chatservice.global.response.ErrorCode;
import com.baekgwa.chatservice.global.response.PageResponse;
import com.baekgwa.chatservice.model.chat.member.entity.ChatRoomMemberEntity;
import com.baekgwa.chatservice.model.chat.member.repository.ChatRoomMemberRepository;
import com.baekgwa.chatservice.model.chat.room.entity.ChatRoomEntity;
import com.baekgwa.chatservice.model.chat.room.repository.ChatRoomRepository;
import com.baekgwa.chatservice.model.user.entity.UserEntity;
import com.baekgwa.chatservice.model.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * PackageName : com.baekgwa.chatservice.domain.room.service
 * FileName    : RoomService
 * Author      : Baekgwa
 * Date        : 2025-10-05
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-05     Baekgwa               Initial creation
 */
@Service
@RequiredArgsConstructor
public class RoomService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final ChatRoomRepository chatRoomRepository;
	private final ChatRoomMemberRepository chatRoomMemberRepository;

	@Transactional
	public RoomResponse.CreateChatRoomResponse createChatRoom(RoomRequest.CreateChatRoomRequest request, Long userId) {
		// 1. 로그인 유저 정보 조회
		UserEntity findUser = userRepository.findById(userId)
			.orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_USER));

		// 2. 신규 채팅방 생성 및 저장
		ChatRoomEntity newChatRoom = ChatRoomEntity.of(request.getTitle(), findUser,
			passwordEncoder.encode(request.getPassword()));
		ChatRoomEntity savedChatRoom = chatRoomRepository.save(newChatRoom);

		// 3. 채팅방에, 생성자는 기본적으로 채팅 멤버로 등록
		ChatRoomMemberEntity newChatRoomMember = ChatRoomMemberEntity.of(findUser, savedChatRoom);
		chatRoomMemberRepository.save(newChatRoomMember);

		return RoomResponse.CreateChatRoomResponse.from(savedChatRoom);
	}

	@Transactional
	public void joinChatRoom(Long roomId, RoomRequest.JoinChatRoomRequest request, Long userId) {
		// 1. 채팅방 조회
		ChatRoomEntity findChatRoom = chatRoomRepository.findById(roomId)
			.orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_CHAT_ROOM));

		// 2. 로그인 사용자 정보 조회
		UserEntity findUser = userRepository.findById(userId)
			.orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_USER));

		// 3. 채팅방 비밀번호 검증
		if (!passwordEncoder.matches(request.getPassword(), findChatRoom.getPassword())) {
			throw new GlobalException(ErrorCode.INCORRECT_CHAT_ROOM_PASSWORD);
		}

		// 3. 채팅방에 신규 멤버 추가.
		ChatRoomMemberEntity newChatRoomMember = ChatRoomMemberEntity.of(findUser, findChatRoom);
		chatRoomMemberRepository.save(newChatRoomMember);
	}

	@Transactional(readOnly = true)
	public List<RoomResponse.JoinedRoomResponse> getJoinedChatRoomList(Long userId) {
		// 1. 로그인 사용자 정보 조회
		UserEntity findUser = userRepository.findById(userId)
			.orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_USER));

		// 2. 내가 가입한 채팅방 목록 조회
		List<ChatRoomMemberEntity> findChatRoomMemberList = chatRoomMemberRepository.findAllByUser(findUser);

		// 3. DTO 변환
		return findChatRoomMemberList.stream()
			.map(RoomResponse.JoinedRoomResponse::of)
			.toList();
	}

	@Transactional(readOnly = true)
	public PageResponse<RoomResponse.ChatRoomResponse> searchRoomList(
		Integer size, Integer page, String keyword, RoomSortType sort
	) {
		Pageable pageable = PageRequest.of(page, size);

		Page<ChatRoomEntity> chatRoomList = switch (sort) {
			case LATEST -> chatRoomRepository.searchByKeywordLatest(keyword, pageable);
			case OLDEST -> chatRoomRepository.searchByKeywordOldest(keyword, pageable);
		};

		Page<RoomResponse.ChatRoomResponse> dtoPage =
			chatRoomList.map(RoomResponse.ChatRoomResponse::from);

		return PageResponse.of(dtoPage);
	}
}
