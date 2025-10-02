package com.baekgwa.chatservice.global.security.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * PackageName : com.baekgwa.chatservice.global.security.constant
 * FileName    : JwtConstant
 * Author      : Baekgwa
 * Date        : 2025-10-02
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-02     Baekgwa               Initial creation
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtConstant {
	public static final String CLAIM_KEY_USER_ID = "id";
	public static final String ACCESS_TOKEN_COOKIE_NAME = "accessToken";
}