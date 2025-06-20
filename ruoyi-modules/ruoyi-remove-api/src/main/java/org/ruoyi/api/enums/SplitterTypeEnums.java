package org.ruoyi.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * @author Peng WenDeng
 * @email pengwendeng@huice.com
 * @time 2025-06-20 17-03
 * @description
 */
/**
 * 文档划分策略枚举
 * 1. 按字符数量划分
 * 2. 专为代码设计的切分器
 * 3. 按 Markdown 结构划分
 * 4. 按 Token 数量划分
 */
@Getter
@AllArgsConstructor
public enum SplitterTypeEnums {

    /**
     * 按字符数量划分
     */
    CHAR_COUNT(1, "按字符数量划分"),

    /**
     * 专为代码设计的切分器
     */
    CODE(2, "专为代码设计的切分器"),

    /**
     * 按 Markdown 结构划分
     */
    MARKDOWN(3, "按 Markdown 结构划分"),

    /**
     * 按 Token 数量划分
     */
    TOKEN_COUNT(4, "按 Token 数量划分");

    private final int type;
    private final String description;

    public static SplitterTypeEnums fromType(int type) {
        for (SplitterTypeEnums value : values()) {
            if (value.getType() == type) {
                return value;
            }
        }
        throw new IllegalArgumentException("不支持的划分类型: " + type);
    }
}

