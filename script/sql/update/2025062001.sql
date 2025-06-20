alter table knowledge_attach
    modify vector_status tinyint(1) default 10 not null comment '写入向量数据库状态10未开始，20进行中，30已完成，40出现异常';