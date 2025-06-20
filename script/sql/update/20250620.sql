alter table knowledge_attach
    add md5 varchar(512) null comment '文件计算的md5值，文件的唯一标识';

alter table knowledge_attach
drop key idx_kname;

alter table knowledge_attach
    add constraint idx_kname
        unique (kid, md5);

