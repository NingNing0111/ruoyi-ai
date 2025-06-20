package org.ruoyi.vector.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.ruoyi.vector.domain.VectorDbInfo;

import java.io.Serializable;

/**
 * @author Zhang De Ning
 * @email zhangdening@huice.com
 * @time 2025-06-18 15:43
 * @description
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = VectorDbInfo.class)
public class VectorDBInfoVo implements Serializable {

	private Long id;

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
	private String connectParams;

}
