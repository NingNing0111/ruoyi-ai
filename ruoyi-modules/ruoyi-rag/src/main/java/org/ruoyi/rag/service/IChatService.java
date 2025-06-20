package org.ruoyi.rag.service;

import org.ruoyi.rag.domain.request.ChatRequest;
import org.springframework.ai.chat.model.ChatResponse;
import reactor.core.publisher.Flux;

/**
 * @author Zhang De Ning
 * @email zhangdening@huice.com
 * @time 2025-06-19 11:25
 * @description
 */

public interface IChatService {

    ChatResponse chat(ChatRequest chatRequest);
}
