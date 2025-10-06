package com.baekgwa.chatservice.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * PackageName : com.baekgwa.chatservice.global.config
 * FileName    : JacksonConfig
 * Author      : Baekgwa
 * Date        : 2025-10-06
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-06     Baekgwa               Initial creation
 */
@Configuration
public class JacksonConfig {

	@Primary
	@Bean("defaultObjectMapper")
	public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
		return builder.build();
	}
}
