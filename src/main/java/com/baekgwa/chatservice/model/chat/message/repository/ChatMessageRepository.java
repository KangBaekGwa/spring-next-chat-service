package com.baekgwa.chatservice.model.chat.message.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.baekgwa.chatservice.model.chat.message.entity.ChatMessageEntity;
import com.baekgwa.chatservice.model.chat.room.entity.ChatRoomEntity;

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

	// 최초 조회용 (가장 최신 메시지 N개)
	@Query("SELECT cm FROM ChatMessageEntity cm WHERE cm.chatRoom = :chatRoom ORDER BY cm.sequence DESC")
	Slice<ChatMessageEntity> findInitialMessages(@Param("chatRoom") ChatRoomEntity chatRoom, Pageable pageable);

	// 과거 메시지 조회용 (cursor 이전 메시지 N개)
	@Query("SELECT cm FROM ChatMessageEntity cm WHERE cm.chatRoom = :chatRoom AND cm.sequence < :sequence ORDER BY cm.sequence DESC")
	Slice<ChatMessageEntity> findMessagesBefore(@Param("chatRoom") ChatRoomEntity chatRoom,
		@Param("sequence") Long sequence, Pageable pageable);

	// 최신 메시지 조회용 (cursor 이후 메시지 N개)
	@Query("SELECT cm FROM ChatMessageEntity cm WHERE cm.chatRoom = :chatRoom AND cm.sequence > :sequence ORDER BY cm.sequence ASC")
	Slice<ChatMessageEntity> findMessagesAfter(@Param("chatRoom") ChatRoomEntity chatRoom,
		@Param("sequence") Long sequence, Pageable pageable);
}
