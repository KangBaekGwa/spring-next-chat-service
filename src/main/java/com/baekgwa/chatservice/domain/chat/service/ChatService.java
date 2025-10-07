package com.baekgwa.chatservice.domain.chat.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baekgwa.chatservice.domain.chat.dto.ChatResponse;
import com.baekgwa.chatservice.domain.chat.type.MessageDirection;
import com.baekgwa.chatservice.global.exception.GlobalException;
import com.baekgwa.chatservice.global.response.CursorResponse;
import com.baekgwa.chatservice.global.response.ErrorCode;
import com.baekgwa.chatservice.model.chat.member.repository.ChatRoomMemberRepository;
import com.baekgwa.chatservice.model.chat.message.entity.ChatMessageEntity;
import com.baekgwa.chatservice.model.chat.message.repository.ChatMessageRepository;
import com.baekgwa.chatservice.model.chat.room.entity.ChatRoomEntity;
import com.baekgwa.chatservice.model.chat.room.repository.ChatRoomRepository;
import com.baekgwa.chatservice.model.user.entity.UserEntity;
import com.baekgwa.chatservice.model.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * PackageName : com.baekgwa.chatservice.domain.chat.service
 * FileName    : ChatService
 * Author      : Baekgwa
 * Date        : 2025-10-04
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-04     Baekgwa               Initial creation
 */
@Service
@RequiredArgsConstructor
public class ChatService {

	private final UserRepository userRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final ChatRoomMemberRepository chatRoomMemberRepository;
	private final ChatMessageRepository chatMessageRepository;

	@Transactional
	public ChatMessageEntity sendMessage(Long senderId, Long roomId, String content, long sequence) {
		UserEntity sender = userRepository.findById(senderId)
			.orElseThrow(() -> new IllegalArgumentException("Sender not found"));
		ChatRoomEntity chatRoom = chatRoomRepository.findById(roomId)
			.orElseThrow(() -> new IllegalArgumentException("Chat room not found"));

		boolean isMember = chatRoomMemberRepository.existsByUserAndChatRoom(sender, chatRoom);
		if (!isMember) {
			throw new IllegalStateException("User is not a member of this chat room");
		}

		ChatMessageEntity newMessage = ChatMessageEntity.of(sender, chatRoom, content, sequence);
		return chatMessageRepository.save(newMessage);
	}

	@Transactional(readOnly = true)
	public boolean isRoomMember(Long userId, Long roomId) {
		UserEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("Sender not found"));
		ChatRoomEntity chatRoom = chatRoomRepository.findById(roomId)
			.orElseThrow(() -> new IllegalArgumentException("Chat room not found"));

		return chatRoomMemberRepository.existsByUserAndChatRoom(user, chatRoom);
	}

	@Transactional(readOnly = true)
	public CursorResponse<ChatResponse.ChatMessageDetail> getChatMessage(
		Long userId, Long roomId, Long sequence, Integer size, MessageDirection direction
	) {
		// 1. 회원이 해당 채팅방의 회원인지 확인
		UserEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_USER));
		ChatRoomEntity chatRoom = chatRoomRepository.findById(roomId)
			.orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_CHAT_ROOM));

		if (!chatRoomMemberRepository.existsByUserAndChatRoom(user, chatRoom)) {
			throw new GlobalException(ErrorCode.NOT_ROOM_MEMBER);
		}

		// 2. size + 1 만큼 조회해서 다음 페이지 유무를 확인
		PageRequest pageable = PageRequest.of(0, size + 1);

		// 3. Slice<T> 타입으로 결과를 받음
		Slice<ChatMessageEntity> messagesSlice;

		if (sequence == null) {
			// 최초 조회: 가장 최신 메시지부터 size+1개 조회
			messagesSlice = chatMessageRepository.findInitialMessages(chatRoom, pageable);
		} else {
			if (direction == MessageDirection.BEFORE) {
				messagesSlice = chatMessageRepository.findMessagesBefore(chatRoom, sequence, pageable);
			} else { // AFTER
				messagesSlice = chatMessageRepository.findMessagesAfter(chatRoom, sequence, pageable);
			}
		}

		List<ChatMessageEntity> messages = new ArrayList<>(messagesSlice.getContent());
		boolean hasNext = messages.size() > size;
		if (hasNext) {
			messages.removeLast();
		}

		Long nextCursor = null;
		if (!messages.isEmpty()) {
			if (direction == MessageDirection.AFTER) {
				nextCursor = messages.getFirst().getSequence();
				messages = messages.reversed();
			} else {
				nextCursor = messages.getLast().getSequence();
			}
		}

		List<ChatResponse.ChatMessageDetail> findList = messages.stream()
			.map(ChatResponse.ChatMessageDetail::of)
			.toList();

		return CursorResponse.of(findList, nextCursor, hasNext);
	}
}
