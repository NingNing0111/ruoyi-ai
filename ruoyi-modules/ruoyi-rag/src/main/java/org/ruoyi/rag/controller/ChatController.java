package org.ruoyi.rag.controller;

import lombok.RequiredArgsConstructor;
import org.ruoyi.common.web.core.BaseController;
import org.ruoyi.rag.domain.request.ChatRequest;
import org.ruoyi.rag.service.IChatService;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * @author Zhang De Ning
 * @email zhangdening@huice.com
 * @time 2025-06-19 11:24
 * @description
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/rag")
public class ChatController extends BaseController {
    private final IChatService chatService;

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest chatRequest) {
        return chatService.chat(chatRequest);
    }
}
