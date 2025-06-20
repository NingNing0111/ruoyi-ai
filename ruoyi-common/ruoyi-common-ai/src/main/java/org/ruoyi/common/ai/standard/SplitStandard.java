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
    /**
     * 知识库id
     */
    private Integer kId;
    /**
     * 文档权重
     */
    private Integer score;
    /**
     * 创建者id
     */
    private Long createBy;
    /**
     * 文档id
     */
    private Long docId;

    // 私有构造方法，只能通过Builder构建
    private SplitStandard(Builder builder) {
        this.separator = builder.separator;
        this.textBlockSize = builder.textBlockSize;
        this.overlapChar = builder.overlapChar;
        this.kId = builder.kId;
        this.score = builder.score;
        this.createBy = builder.createBy;
        this.docId = builder.docId;
    }

    // Getter 方法（可选：用于访问属性）
    public String getSeparator() {
        return separator;
    }

    public Integer getTextBlockSize() {
        return textBlockSize;
    }

    public Integer getOverlapChar() {
        return overlapChar;
    }

    public Integer getKId() {
        return kId;
    }

    public Integer getScore() {
        return score;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public Long getDocId() {
        return docId;
    }

    @Override
    public String toString() {
        return "SplitStandard{" +
                "separator='" + separator + '\'' +
                ", textBlockSize=" + textBlockSize +
                ", overlapChar=" + overlapChar +
                ", kId=" + kId +
                ", score=" + score +
                ", createBy=" + createBy +
                ", docId=" + docId +
                '}';
    }

    // Builder 静态内部类
    public static class Builder {
        private String separator;
        private Integer textBlockSize;
        private Integer overlapChar;
        private Integer kId;
        private Integer score;
        private Long createBy;

        private Long docId;

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

        public Builder kId(Integer kId) {
            this.kId = kId;
            return this;
        }

        public Builder score(Integer score) {
            this.score = score;
            return this;
        }

        public Builder createBy(Long createBy) {
            this.createBy = createBy;
            return this;
        }

        public Builder docId(Long docId) {
            this.docId = docId;
            return this;
        }

        public SplitStandard build() {
            return new SplitStandard(this);
        }
    }

}
