package org.ruoyi.rag.domain.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author Zhang De Ning
 * @email zhangdening@huice.com
 * @time 2025-06-19 10:25
 * @description
 */
@Data
public class ChatRequest {
    /**
     * 知识库ID
     */
    @NotNull(message = "知识库ID不能为空")
    private List<Long> kIds;
    /**
     * 问答
     */
    @NotNull(message = "对话内容不能为空")
    private String prompt;
}
