package com.baekgwa.chatservice.global.environment;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * PackageName : com.baekgwa.chatservice.global.environment
 * FileName    : JwtProperties
 * Author      : Baekgwa
 * Date        : 2025-10-02
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-02     Baekgwa               Initial creation
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "auth")
public class JwtProperties {

	private String secretKey;
	private Long tokenExpirationMin;
}
