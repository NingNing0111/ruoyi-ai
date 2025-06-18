package org.ruoyi.vector.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.common.core.validate.AddGroup;
import org.ruoyi.common.core.validate.EditGroup;
import org.ruoyi.core.domain.BaseEntity;
import org.ruoyi.vector.domain.VectorDbInfo;

/**
 * @author Zhang De Ning
 * @email zhangdening@huice.com
 * @time 2025-06-18 15:29
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = VectorDbInfo.class, reverseConvertGenerate = false)
public class VectorDBInfoBo extends BaseEntity {
    /**
     * 向量数据库类型，如milvus、pgvector等，用Int常量表示
     */
    @NotNull(message = "向量数据库类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer type;

    /**
     * 向量库主机名，域名/ip
     */
    @NotNull(message = "主机名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String hostname;

    /**
     * 端口号
     */
    @NotNull(message = "端口不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer port;

    /**
     * 向量维度
     */
    @NotNull(message = "向量维度不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer dimension;

    /**
     * 数据库名，有些向量数据库需要，如pgvector
     */
    private String dbName;

    /**
     * 表名，部分db需要
     */
    private String tableName;

    /**
     * 集合名，部分db需要
     */
    private String collection;

    /**
     * 索引类型
     */
    private Integer indexType;

    /**
     * 指标类型
     */
    private Integer metricType;

    /**
     * ID字段名称
     */
    private String idFieldName;

    /**
     * Meta字段名称
     */
    private String metadataFieldName;

    /**
     * 向量字段名称
     */
    private String embeddingFieldName;

    /**
     * ID类型，如Long/String等
     */
    private Integer idType;

    /**
     * 距离计算类型
     */
    private Integer distanceType;

    /**
     * 模式名（Schema Name）
     */
    private String schemaName;

    /**
     * 是否开启Schema验证
     */
    private Boolean schemaValidation;

    /**
     * 是否自动初始化Schema（建表、清空）
     */
    @NotNull(message = "初始化Schema值不能为空", groups = { AddGroup.class })
    private Boolean initializeSchema;

    /**
     * 是否删除已存在的向量存储表
     */
    private Boolean removeExistingVectorStoreTable;

    /**
     * 最大文档批量处理数量
     */
    private Integer maxDocumentBatchSize;

    /**
     * 连接用户名
     */
    @NotNull(message = "用户名不能为空", groups = { AddGroup.class,EditGroup.class })
    private String username;

    /**
     * 连接密码
     */
    @NotNull(message = "认证密码不能为空", groups = { AddGroup.class,EditGroup.class })
    private String password;

    /**
     * 连接参数，JSON格式
     */
    private String connectParams;
}
