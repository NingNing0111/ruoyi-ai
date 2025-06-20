package org.ruoyi.common.ai.helper;

/**
 * @author Zhang De Ning
 * @email zhangdening@huice.com
 * @time 2025-06-20 16:04
 * @description 字符串处理
 */
public abstract class StringHelper {
    private String content;

    public StringHelper(String content) {
        this.content = content;
    }

    public abstract String parse();
}
