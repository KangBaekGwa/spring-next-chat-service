package com.baekgwa.chatservice.domain.chat.service;

import static com.baekgwa.chatservice.global.constant.MessageConstant.*;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.baekgwa.chatservice.model.chat.message.repository.ChatMessageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * PackageName : com.baekgwa.chatservice.domain.chat.service
 * FileName    : ChatMessageSequenceService
 * Author      : Baekgwa
 * Date        : 2025-10-04
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-04     Baekgwa               Initial creation
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageSequenceService {

	private final RedisTemplate<String, String> redisTemplate;
	private final ChatMessageRepository chatMessageRepository;

	public Long getNextSequence(Long roomId) {
		String key = SEQUENCE_KEY_PREFIX + roomId;

		// 1. Redis 에 시퀀스 키가 있는지 확인
		if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
			log.warn("{}방 Sequence 값 초기화 진행", roomId);
			String lockKey = LOCK_KEY_PREFIX + roomId;
			Boolean locked = redisTemplate.opsForValue()
				.setIfAbsent(lockKey, "locked", Duration.ofSeconds(10));

			if (Boolean.TRUE.equals(locked)) {
				try {
					Long lastSequence = chatMessageRepository.findLastSequenceByRoomId(roomId)
						.orElse(0L); // 만약 방이 만들어지고, 아무 메시지도 없었다면 0으로 초기화 해아함
					redisTemplate.opsForValue().set(key, String.valueOf(lastSequence));
					log.warn("{}방 Sequence = [{}] 으로 초기화 완료", roomId, lastSequence);
				} finally {
					redisTemplate.delete(lockKey);
				}
			} else {
				try {
					log.warn("{}방 Sequence 초기화 대기 중 (10ms). 이미 타 쓰레드에서 초기화 진행 중.", roomId);
					Thread.sleep(10); //잠시 대기
				} catch (InterruptedException e) {
					log.warn("Sequence 초기화 대기 중 스레드가 중단되었습니다. Room ID: {}", roomId, e);
					Thread.currentThread().interrupt();
					throw new IllegalStateException("시퀀스 생성 대기 중 스레드가 중단되었습니다.", e);
				}
			}
		}

		return redisTemplate.opsForValue().increment(key);
	}

	/**
	 * 신규 생성된 방의 Message Sequence 값을 생성
	 * @param roomId 방 PK
	 */
	public void setNewRoomMessageSequence(Long roomId) {
		String key = SEQUENCE_KEY_PREFIX + roomId;
		redisTemplate.opsForValue().set(key, "0");
	}

	/**
	 * Application 초기화 시, Redis 에 Sequence 값을 저장하기 위해 initialize 진행 시, 필요
	 * @param roomId 방 PK
	 * @param sequence 시퀸스 값
	 */
	public Boolean initRoomMessageSequence(Long roomId, Long sequence) {
		String key = SEQUENCE_KEY_PREFIX + roomId;
		return redisTemplate.opsForValue().setIfAbsent(key, String.valueOf(sequence));
	}
}
