-- auto-generated definition
create table user
(
    id          bigint auto_increment comment '主键id'
        primary key,
    userName    varchar(256)                       null comment '用户名',
    userAccount varchar(256)                       null comment '登录账号',
    passWord    varchar(256)                       null comment '登陆密码',
    gender      tinyint                            null comment '性别',
    phone       varchar(128)                       null comment '手机号',
    email       varchar(128)                       null comment '邮箱',
    avatarUrl   varchar(1024)                      null comment '头像',
    status      tinyint  default 0                 null comment '状态',
    userRole    tinyint  default 0                 null comment '用户角色',
    createTime  datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP null comment '更新时间',
    isDelete    tinyint  default 0                 null comment '是否删除',
    inviteCode  varchar(256)                       null comment '邀请码',
    tags        varchar(1024)                      null comment '标签列表',
    profile     varchar(512)                       null comment '个人简介'
);

