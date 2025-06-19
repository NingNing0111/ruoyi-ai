package org.ruoyi.common.ai.vector;

import java.io.Serializable;

/**
 * @author Zhang De Ning
 * @email zhangdening@huice.com
 * @time 2025-06-18 12:43
 * @description
 */
public interface VectorDB extends Serializable {
    /**
     * 是否自动初始化。若自动初始化，则会自动删数据，建表
     * @return 是否自动初始化
     */
    Boolean initializeSchema();
    /**
     * 向量数据库类型
     * @return 向量数据库类型
     */
    Integer getType();

    /**
     * 主机名
     * @return 向量数据库的主机名称
     */
    String getHostname();

    /**
     * 返回向量数据库端口
     * @return 向量数据库端口
     */
    Integer getPort();         // 端口

    /**
     * 返回向量维度
     * @return 向量维度
     */
    Integer getDimension();

    /**
     * 返回数据库名（可选）
     * @return 数据库名
     */
    String getDbName();

    /**
     * 返回表名（可选）
     * @return 表名
     */
    String getTableName();

    /**
     * 返回集合名
     * @return 集合名
     */
    String getCollection();

    /**
     * 返回索引类型
     * @return 索引类型
     */
    Integer getIndexType();

    /**
     * 返回指标类型
     * @return 指标类型
     */
    Integer getMetricType();

    /**
     * 返回ID字段名称
     * @return ID字段名称
     */
    String getIdFieldName();

    /**
     * 返回meta字段名称
     * @return meta字段名称
     */
    String getMetadataFieldName();

    /**
     * 返回向量字段
     * @return 向量字段
     */
    String getEmbeddingFieldName();

    /**
     * 返回主机地址
     * @return 主机地址
     */
    default String getHostAddress() {
        return getHostname() + ":" + getPort();
    }

    /**
     * 返回是否删除已经存在的表
     * @return 是否删除已经存在的表
     */
    Boolean getRemoveExistingVectorStoreTable();

    /**
     * 返回ID类型
     * @return ID类型
     */
    Integer getIdType();

    /**
     * 返回距离计算类型
     * @return 距离计算类型
     */
    Integer getDistanceType();

    /**
     * 返回模式名称
     * @return 模式名称
     */
    String getSchemaName();

    /**
     * 启用模式和表名验证以确保它们是有效且现有的对象
     * @return 是否启用校验
     */
    Boolean schemaValidation();

    /**
     * 返回最大批量处理量
     * @return 最大批量处理量
     */
    Integer getMaxDocumentBatchSize();

    /**
     * 获取用户名
     * @return 用户名
     */
    String getUsername();

    /**
     * 获取密码
     * @return 密码
     */
    String getPassword();

}
