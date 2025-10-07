package com.baekgwa.chatservice.global.response;

import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * PackageName : com.baekgwa.chatservice.global.response
 * FileName    : CursorResponse
 * Author      : Baekgwa
 * Date        : 2025-10-07
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-07     Baekgwa               Initial creation
 */
@Getter
@Builder(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CursorResponse<T> {
	private final List<T> content;
	private final Long nextCursor;
	private final boolean hasNext;

	public static <T> CursorResponse<T> of(List<T> content, Long nextCursor, boolean hasNext) {
		return CursorResponse.<T>builder()
			.content(content)
			.nextCursor(nextCursor)
			.hasNext(hasNext)
			.build();
	}
}
