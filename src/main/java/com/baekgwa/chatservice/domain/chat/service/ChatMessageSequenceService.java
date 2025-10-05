package com.baekgwa.chatservice.domain.chat.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

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
@Service
@RequiredArgsConstructor
public class ChatMessageSequenceService {

	private final ConcurrentHashMap<Long, AtomicLong> map = new ConcurrentHashMap<>();

	//todo : Redis 로, Atomic 하게 Integer ++ 처리해서 가져올 것.
	//단, Redis 가 리붓되거나 그런 상황을 예측해서, 없을 경우, 1로 만들기 전에, DB 에서 먼저 조회를 실시
	public long getNextSequence(Long roomId) {
		// roomId에 해당하는 값이 없으면 0을 가진 AtomicLong을 새로 생성
		// putIfAbsent는 해당 key가 없을 때만 put을 실행하므로 동시성 이슈가 없음
		map.putIfAbsent(roomId, new AtomicLong(0));

		// 값을 가져와서 1 증가시키는 연산을 원자적으로 수행
		return map.get(roomId).incrementAndGet();
	}
}
