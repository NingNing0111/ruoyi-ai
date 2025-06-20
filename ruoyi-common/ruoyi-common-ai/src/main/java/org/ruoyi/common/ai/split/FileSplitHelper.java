package org.ruoyi.common.ai.split;

import org.springframework.ai.document.Document;

import java.util.List;

/**
 * @author Zhang De Ning
 * @email zhangdening@huice.com
 * @time 2025-06-19 17:06
 * @description
 */
public interface FileSplitHelper {
    /**
     * 分割切片
     * @return
     */
    List<Document> split();
}
