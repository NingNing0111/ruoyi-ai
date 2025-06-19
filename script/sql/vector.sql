DROP TABLE IF EXISTS `vector_db_info`;
CREATE TABLE `vector_db_info`
(
    `id`                                 bigint AUTO_INCREMENT                                         NOT NULL COMMENT '主键ID',
    `label`                              varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配置标签',
    `type`                               int                                                           NOT NULL COMMENT '向量数据库类型，如milvus、pgvector等，用Int常量表示',
    `hostname`                           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '向量库主机名，域名/ip',
    `port`                               int                                                           NULL DEFAULT NULL COMMENT '端口号',
    `dimension`                          int                                                           NULL DEFAULT NULL COMMENT '向量维度',

    `db_name`                            varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据库名，有些向量数据库需要，如pgvector',
    `table_name`                         varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表名，部分db需要',
    `collection`                         varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '集合名，部分db需要',

    `index_type`                         int                                                           NULL DEFAULT NULL COMMENT '索引类型',
    `metric_type`                        int                                                           NULL DEFAULT NULL COMMENT '指标类型',
    `id_field_name`                      varchar(128)                                                  NULL DEFAULT NULL COMMENT 'ID字段名称',
    `metadata_field_name`                varchar(128)                                                  NULL DEFAULT NULL COMMENT 'Meta字段名称',
    `embedding_field_name`               varchar(128)                                                  NULL DEFAULT NULL COMMENT '向量字段名称',
    `id_type`                            int                                                           NULL DEFAULT NULL COMMENT 'ID类型，如Long/String等',
    `distance_type`                      int                                                           NULL DEFAULT NULL COMMENT '距离计算类型',

    `schema_name`                        varchar(128)                                                  NULL DEFAULT NULL COMMENT '模式名（Schema Name）',
    `schema_validation`                  bit(1)                                                        NULL DEFAULT b'0' COMMENT '是否开启Schema验证',
    `initialize_schema`                  bit(1)                                                        NULL DEFAULT b'0' COMMENT '是否自动初始化Schema（建表、清空）',
    `remove_existing_vector_store_table` bit(1)                                                        NULL DEFAULT b'0' COMMENT '是否删除已存在的向量存储表',
    `max_document_batch_size`            int                                                           NULL DEFAULT NULL COMMENT '最大文档批量处理数量',

    `username`                           varchar(128)                                                  NULL DEFAULT NULL COMMENT '连接用户名',
    `password`                           varchar(128)                                                  NULL DEFAULT NULL COMMENT '连接密码',
    `params`                             text COMMENT '连接参数，JSON格式',

    `del_flag`                           char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `create_dept`                        bigint                                                        NULL DEFAULT NULL COMMENT '创建部门',
    `create_by`                          bigint                                                        NULL DEFAULT NULL COMMENT '创建者',
    `create_time`                        datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`                          bigint                                                        NULL DEFAULT NULL COMMENT '更新者',
    `update_time`                        datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '向量数据库信息表'
  ROW_FORMAT = Dynamic;
