package com.baekgwa.chatservice.model.chat.message.entity;

import com.baekgwa.chatservice.global.entity.TemporalEntity;
import com.baekgwa.chatservice.model.chat.room.entity.ChatRoomEntity;
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
 * PackageName : com.baekgwa.chatservice.model.chat.message.entity
 * FileName    : ChatMessageEntity
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
@Table(name = "chat_message")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageEntity extends TemporalEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_id", nullable = false)
	private UserEntity sender;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chat_room_id", nullable = false)
	private ChatRoomEntity chatRoom;

	@Column(name = "content", nullable = false, length = 1000)
	private String content;

	@Column(name = "sequence", nullable = false, length = 1000)
	private Long sequence;

	@Builder(access = AccessLevel.PRIVATE)
	private ChatMessageEntity(UserEntity sender, ChatRoomEntity chatRoom, String content, Long sequence) {
		this.sender = sender;
		this.chatRoom = chatRoom;
		this.content = content;
		this.sequence = sequence;
	}

	public static ChatMessageEntity of(UserEntity sender, ChatRoomEntity chatRoom, String content, Long sequence) {
		return ChatMessageEntity
			.builder()
			.sender(sender)
			.chatRoom(chatRoom)
			.content(content)
			.sequence(sequence)
			.build();
	}
}
