package com.baekgwa.chatservice.domain.chat.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		return chatRoomMemberRepository.existsByUserIdAndChatRoomId(userId, roomId);
	}
}
