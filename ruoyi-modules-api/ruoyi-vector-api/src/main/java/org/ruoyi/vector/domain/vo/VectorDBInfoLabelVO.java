package org.ruoyi.vector.domain.vo;

import lombok.Data;

/**
 * @author Zhang De Ning
 * @email zhangdening@huice.com
 * @time 2025-06-20 09:43
 * @description
 */
@Data
public class VectorDBInfoLabelVO {

    /**
     * 向量数据库类型，如milvus、pgvector等，用Int常量表示
     */
    private Integer type;
    private String typeName;

    /**
     * 向量库主机名，域名/ip
     */
    private String hostname;

    /**
     * 端口号
     */
    private Integer port;

    /**
     * 向量维度
     */
    private Integer dimension;
}
