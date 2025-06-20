package org.ruoyi.rag.domain.ao;

import lombok.Data;

/**
 * @author Zhang De Ning
 * @email zhangdening@huice.com
 * @time 2025-06-19 15:06
 * @description
 */
@Data
public class ChunkItem {
    /**
     * 内容
     */
    private String content;
    /**
     * 得分
     */
    private Double score;
}
