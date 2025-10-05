package com.baekgwa.chatservice.model.chat.message.repository;

import org.springframework.data.jpa.repository.JpaRepository;

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
}
