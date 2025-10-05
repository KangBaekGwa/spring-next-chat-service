package com.baekgwa.chatservice.model.chat.room.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.baekgwa.chatservice.model.chat.room.entity.ChatRoomEntity;

/**
 * PackageName : com.baekgwa.chatservice.model.chat.repository
 * FileName    : ChatRoomRepository
 * Author      : Baekgwa
 * Date        : 2025-10-04
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-04     Baekgwa               Initial creation
 */
public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> {
}
