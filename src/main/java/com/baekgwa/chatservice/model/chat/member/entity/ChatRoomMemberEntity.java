package com.baekgwa.chatservice.model.chat.member.entity;

import com.baekgwa.chatservice.global.entity.TemporalEntity;
import com.baekgwa.chatservice.model.chat.room.entity.ChatRoomEntity;
import com.baekgwa.chatservice.model.user.entity.UserEntity;

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
 * PackageName : com.baekgwa.chatservice.model.chat.member.entity
 * FileName    : ChatRoomMemberEntity
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
@Table(name = "chat_room_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomMemberEntity extends TemporalEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chat_room_id", nullable = false)
	private ChatRoomEntity chatRoom;

	@Builder(access = AccessLevel.PRIVATE)
	private ChatRoomMemberEntity(UserEntity user, ChatRoomEntity chatRoom) {
		this.user = user;
		this.chatRoom = chatRoom;
	}

	public static ChatRoomMemberEntity of(UserEntity user, ChatRoomEntity chatRoom) {
		return ChatRoomMemberEntity.builder()
			.user(user)
			.chatRoom(chatRoom)
			.build();
	}
}
