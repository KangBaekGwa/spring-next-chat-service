package com.baekgwa.chatservice.model.user.entity;

import com.baekgwa.chatservice.global.entity.TemporalEntity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * PackageName : com.baekgwa.chatservice.model.user.entity
 * FileName    : UserEntity
 * Author      : Baekgwa
 * Date        : 2025-10-02
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-02     Baekgwa               Initial creation
 */
@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends TemporalEntity {

	@Id
	@Tsid
	@Column(name = "id")
	private Long id;

	@Column(name = "login_id", unique = true)
	private String loginId;

	@Column(name = "password")
	private String password;

	@Column(name = "username", nullable = false, unique = true)
	private String username;

	@Builder(access = AccessLevel.PRIVATE)
	private UserEntity(String loginId, String password, String username) {
		this.loginId = loginId;
		this.password = password;
		this.username = username;
	}

	public static UserEntity of(String loginId, String password, String username) {
		return UserEntity
			.builder()
			.loginId(loginId)
			.password(password)
			.username(username)
			.build();
	}
}
