package com.baekgwa.chatservice.global.response;

import java.util.Arrays;
import java.util.Optional;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;

/**
 * PackageName : com.baekgwa.chatservice.global.response
 * FileName    : RequestUtil
 * Author      : Baekgwa
 * Date        : 2025-10-04
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-04     Baekgwa               Initial creation
 */
@UtilityClass
public class RequestUtil {

	/**
	 * 요청(HttpServletRequest)에 포함된 쿠키 배열에서 특정 이름의 쿠키 값을 찾아 반환합니다.
	 * @param request HttpServletRequest 객체
	 * @param cookieName 찾고자 하는 쿠키의 이름
	 * @return 쿠키 값이 담긴 Optional 객체. 쿠키가 없으면 Optional.empty()를 반환합니다.
	 */
	public Optional<String> getCookieValue(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();

		if (cookies == null) {
			return Optional.empty();
		}

		return Arrays.stream(cookies)
			.filter(cookie -> cookie.getName().equals(cookieName))
			.map(Cookie::getValue)
			.findFirst();
	}
}
