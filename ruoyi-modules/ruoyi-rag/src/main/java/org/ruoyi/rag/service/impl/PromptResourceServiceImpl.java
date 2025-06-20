package org.ruoyi.rag.service.impl;

import lombok.RequiredArgsConstructor;
import org.ruoyi.common.core.exception.base.BaseException;
import org.ruoyi.rag.service.IPromptResourceService;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Zhang De Ning
 * @email zhangdening@huice.com
 * @time 2025-06-19 15:38
 * @description
 */
@Service
@RequiredArgsConstructor
public class PromptResourceServiceImpl implements IPromptResourceService {



    @Override
    public Message createMessageFromClassResource(String path, OpenAiApi.ChatCompletionMessage.Role role, Map<String, Object> params){
        try {
            ClassPathResource resource = new ClassPathResource(path);
            switch (role) {
                case SYSTEM -> {
                    SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(resource);
                    return systemPromptTemplate.createMessage(params);
                }
                case USER -> {
                    PromptTemplate template = PromptTemplate.builder().resource(resource).variables(params).build();
                    return template.createMessage();
                }
                default -> {
                    throw new BaseException("加载未知类型的提示词: " + role.name());
                }
            }
        } catch (Exception e) {
            throw new BaseException("Failed to load prompt from resource: " + path);
        }
    }

    @Override
    public Message createMessageFromClassResource(String path, OpenAiApi.ChatCompletionMessage.Role role) {
        try {
            ClassPathResource resource = new ClassPathResource(path);
            switch (role) {
                case SYSTEM -> {
                    SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(resource);
                    return systemPromptTemplate.createMessage();
                }
                case USER -> {
                    PromptTemplate template = PromptTemplate.builder().resource(resource).build();
                    return template.createMessage();
                }
                default -> {
                    throw new BaseException("加载未知类型的提示词: " + role.name());
                }
            }
        } catch (Exception e) {
            throw new BaseException("Failed to load prompt from resource: " + path);
        }
    }


}
