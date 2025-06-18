alter table knowledge_info
    add splitter_type tinyint null comment '文档划分策略（1表示按字符数量进行划分，2为专为代码设计的切分器，3为按 Markdown 结构进行划分，4为按 Token 数量进行划分）';

alter table knowledge_info
    add v_id int null comment '接入的向量数据库id';

alter table knowledge_info
    add type tinyint null comment '知识库的类型（1为文本知识库，2为图片知识库）';


