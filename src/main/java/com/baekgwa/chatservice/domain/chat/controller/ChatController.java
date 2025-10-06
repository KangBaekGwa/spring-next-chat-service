package com.baekgwa.chatservice.domain.chat.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * PackageName : com.baekgwa.chatservice.domain.chat.controller
 * FileName    : ChatController
 * Author      : Baekgwa
 * Date        : 2025-10-05
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-05     Baekgwa               Initial creation
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
@Tag(name = "Chat Controller", description = "채팅 도메인")
public class ChatController {
}
