package org.ruoyi.common.ai.llm;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.moderation.ModerationModel;

/**
 * @author Zhang De Ning
 * @email zhangdening@huice.com
 * @time 2025-06-19 16:51
 * @description LLM提供接口
 */
public interface LLMProvider {
    /**
     * 获取一个对话模型
     * @return 对话模型
     */
    ChatModel chatModel();

    /**
     * 获取一个图片模型
     * @return 图片模型
     */
    ImageModel imageModel();

    /**
     * 获取一个向量模型
     * @return 向量模型
     */
    EmbeddingModel embeddingModel();

    /**
     * 获取一个多模态模型
     * @return 多模态模型
     */
    ModerationModel moderationModel();
}
