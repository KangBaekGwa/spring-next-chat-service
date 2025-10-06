package com.baekgwa.chatservice.global.init;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.baekgwa.chatservice.domain.chat.service.ChatMessageSequenceService;
import com.baekgwa.chatservice.model.chat.message.repository.ChatMessageRepository;
import com.baekgwa.chatservice.model.chat.room.entity.ChatRoomEntity;
import com.baekgwa.chatservice.model.chat.room.repository.ChatRoomRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * PackageName : com.baekgwa.chatservice.global.init
 * FileName    : MessageSequenceInitializer
 * Author      : Baekgwa
 * Date        : 2025-10-06
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-06     Baekgwa               Initial creation
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MessageSequenceInitializer implements ApplicationListener<ApplicationReadyEvent> {

	private final ChatRoomRepository chatRoomRepository;
	private final ChatMessageRepository chatMessageRepository;
	private final ChatMessageSequenceService sequenceService;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		log.info("=============== Redis Room Message Sequence 초기화 작업을 시작합니다. ===============");
		List<ChatRoomEntity> allRoomList = chatRoomRepository.findAll();
		AtomicLong initCount = new AtomicLong();

		allRoomList.forEach(room -> {
			Long lastSequence = chatMessageRepository.findLastSequenceByRoomId(room.getId()).orElse(0L);
			boolean result = sequenceService.initRoomMessageSequence(room.getId(), lastSequence);

			if(result) {
				log.info("Room ID [{}]: Redis 에 시퀀스 값 [{}]을 새로 설정했습니다.", room.getId(), lastSequence);
				initCount.getAndIncrement();
			}
		});
		log.info("=============== Redis Sequence 초기화 완료 (총 {}개) ===============", initCount);
	}
}
