package org.ruoyi.rag.domain.ao;

import lombok.Data;

import java.util.List;

/**
 * @author Zhang De Ning
 * @email zhangdening@huice.com
 * @time 2025-06-19 15:06
 * @description
 */
@Data
public class DocumentAo {
    /**
     * 知识库名称
     */
    private String knowledgeBaseName;
    /**
     * 知识库描述
     */
    private String knowledgeBaseDescription;
    /**
     * 相关文档
     */
    private List<ChunkItem> chunks;
}
