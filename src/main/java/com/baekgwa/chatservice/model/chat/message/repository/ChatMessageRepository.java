package com.baekgwa.chatservice.model.chat.message.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.baekgwa.chatservice.model.chat.message.entity.ChatMessageEntity;

/**
 * PackageName : com.baekgwa.chatservice.model.chat.message.repository
 * FileName    : ChatMessageRepository
 * Author      : Baekgwa
 * Date        : 2025-10-04
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-04     Baekgwa               Initial creation
 */
public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {

	@Query("SELECT MAX(cm.sequence) FROM ChatMessageEntity cm WHERE cm.chatRoom.id = :roomId")
	Optional<Long> findLastSequenceByRoomId(@Param("roomId") Long roomId);
}
