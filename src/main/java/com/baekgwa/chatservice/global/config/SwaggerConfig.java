package com.baekgwa.chatservice.global.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

/**
 * PackageName : com.baekgwa.chatservice.global.config
 * FileName    : SwaggerConfig
 * Author      : Baekgwa
 * Date        : 2025-10-05
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-05     Baekgwa               Initial creation
 */
@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
			.info(new Info()
				.title("채팅 서비스 API 문서")
				.version("v1")
				.description("채팅 서비스 API 문서")
				.contact(new Contact()
					.name("Baekgwa")
					.email("ksu9801@gmail.com")
				)
			)
			.servers(List.of(
				new Server().url("http://localhost:8080").description("로컬 서버")
			));
	}
}
