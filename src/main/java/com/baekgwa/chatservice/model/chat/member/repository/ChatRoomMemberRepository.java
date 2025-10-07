package com.baekgwa.chatservice.model.chat.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.baekgwa.chatservice.model.chat.member.entity.ChatRoomMemberEntity;
import com.baekgwa.chatservice.model.chat.room.entity.ChatRoomEntity;
import com.baekgwa.chatservice.model.user.entity.UserEntity;

/**
 * PackageName : com.baekgwa.chatservice.model.chat.member.repository
 * FileName    : ChatRoomMemberRepository
 * Author      : Baekgwa
 * Date        : 2025-10-04
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-04     Baekgwa               Initial creation
 */
public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMemberEntity, Long> {
	boolean existsByUserAndChatRoom(UserEntity user, ChatRoomEntity chatRoom);

	@EntityGraph(attributePaths = {"chatRoom"})
	List<ChatRoomMemberEntity> findAllByUser(UserEntity findUser);
}
