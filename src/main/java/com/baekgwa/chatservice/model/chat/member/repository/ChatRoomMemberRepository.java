package com.baekgwa.chatservice.model.chat.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
	boolean existsByUserAndChatRoom(UserEntity sender, ChatRoomEntity chatRoom);

	@Query("SELECT crm FROM ChatRoomMemberEntity crm WHERE crm.user.id = :userId AND crm.chatRoom.id = :chatRoomId")
	boolean existsByUserIdAndChatRoomId(@Param("userId") Long userId, @Param("chatRoomId") Long chatRoomId);
}
