package org.ruoyi.vector.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.common.ai.vector.VectorDB;
import org.ruoyi.core.domain.BaseEntity;

import java.util.Date;

/**
 * 向量数据库信息表
 *
 * @author admin
 * @TableName vector_db_info
 */
@TableName(value = "vector_db_info")
@Data
@EqualsAndHashCode(callSuper = true)
public class VectorDbInfo extends BaseEntity implements VectorDB {

	/**
	 * 主键ID
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 标签
	 */
	private String label;

	/**
	 * 向量数据库类型，如milvus、pgvector等，用Int常量表示
	 */
	private Integer type;

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
	private String username;

	/**
	 * 连接密码
	 */
	private String password;

	/**
	 * 连接参数，JSON格式
	 */
	@TableField(value = "params")
	private String connectParams;

	@Override
	public Boolean initializeSchema() {
		return this.initializeSchema;
	}

	@Override
	public Boolean schemaValidation() {
		return this.schemaValidation;
	}

}