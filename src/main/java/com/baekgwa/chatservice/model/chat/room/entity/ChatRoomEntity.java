package com.baekgwa.chatservice.model.chat.room.entity;

import com.baekgwa.chatservice.global.entity.TemporalEntity;
import com.baekgwa.chatservice.model.user.entity.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * PackageName : com.baekgwa.chatservice.model.chat.entity
 * FileName    : ChatRoomEntity
 * Author      : Baekgwa
 * Date        : 2025-10-04
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-04     Baekgwa               Initial creation
 */
@Entity
@Getter
@Table(name = "chat_room")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomEntity extends TemporalEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "title", nullable = false)
	private String title;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creator_id", nullable = false)
	private UserEntity owner;

	@Column(name = "password")
	private String password;

	@Builder(access = AccessLevel.PRIVATE)
	public ChatRoomEntity(String title, UserEntity owner, String password) {
		this.title = title;
		this.owner = owner;
		this.password = password;
	}

	public static ChatRoomEntity of(String title, UserEntity owner, String password) {
		return ChatRoomEntity.builder()
			.title(title)
			.owner(owner)
			.password(password)
			.build();
	}
}
