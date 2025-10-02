package com.baekgwa.chatservice.model.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.baekgwa.chatservice.model.user.entity.UserEntity;

/**
 * PackageName : com.baekgwa.chatservice.model.user.repository
 * FileName    : UserRepository
 * Author      : Baekgwa
 * Date        : 2025-10-02
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-02     Baekgwa               Initial creation
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findByLoginId(String loginId);
}
