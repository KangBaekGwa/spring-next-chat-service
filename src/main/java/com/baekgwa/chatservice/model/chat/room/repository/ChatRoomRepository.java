package com.baekgwa.chatservice.model.chat.room.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

	@Query("SELECT cr FROM ChatRoomEntity cr WHERE LOWER(cr.title) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY cr.createdAt DESC")
	Page<ChatRoomEntity> searchByKeywordLatest(@Param("keyword") String keyword, Pageable pageable);

	@Query("SELECT cr FROM ChatRoomEntity cr WHERE LOWER(cr.title) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY cr.createdAt ASC")
	Page<ChatRoomEntity> searchByKeywordOldest(String keyword, Pageable pageable);
}
