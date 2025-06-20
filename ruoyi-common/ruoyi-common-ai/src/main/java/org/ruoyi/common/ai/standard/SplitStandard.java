package org.ruoyi.common.ai.standard;

/**
 * @author Peng WenDeng
 * @email pengwendeng@huice.com
 * @time 2025-06-20 10-15
 * @description
 */
public class SplitStandard {
    /**
     * 分隔符
     */
    private String separator;
    /**
     * 文本块大小
     */
    private Integer textBlockSize;
    /**
     * 重叠字符大小
     */
    private Integer overlapChar;

    // 私有构造方法
    private SplitStandard(Builder builder) {
        this.separator = builder.separator;
        this.textBlockSize = builder.textBlockSize;
        this.overlapChar = builder.overlapChar;
    }

    // Getter 方法
    public String getSeparator() {
        return separator;
    }

    public Integer getTextBlockSize() {
        return textBlockSize;
    }

    public Integer getOverlapChar() {
        return overlapChar;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public void setTextBlockSize(Integer textBlockSize) {
        this.textBlockSize = textBlockSize;
    }

    public void setOverlapChar(Integer overlapChar) {
        this.overlapChar = overlapChar;
    }

    // 静态内部 Builder 类
    public static class Builder {
        private String separator;
        private Integer textBlockSize;
        private Integer overlapChar;

        public Builder separator(String separator) {
            this.separator = separator;
            return this;
        }

        public Builder textBlockSize(Integer textBlockSize) {
            this.textBlockSize = textBlockSize;
            return this;
        }

        public Builder overlapChar(Integer overlapChar) {
            this.overlapChar = overlapChar;
            return this;
        }

        public SplitStandard build() {
            return new SplitStandard(this);
        }
    }

    // 可选：静态方法简化 Builder 创建
    public static Builder builder() {
        return new Builder();
    }
}