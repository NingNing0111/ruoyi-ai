package org.ruoyi.rag.service;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.ChatPromptTemplate;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.api.OpenAiApi;

import java.util.Map;

/**
 * @author Zhang De Ning
 * @email zhangdening@huice.com
 * @time 2025-06-19 15:35
 * @description
 */
public interface IPromptResourceService {
    /**
     * 从类路径下 加载提示词
     * @param path
     * @return
     */
    Message createMessageFromClassResource(String path, OpenAiApi.ChatCompletionMessage.Role role, Map<String, Object> params);

    Message createMessageFromClassResource(String path, OpenAiApi.ChatCompletionMessage.Role role);

}
