DROP TABLE IF EXISTS conf;
DROP TABLE IF EXISTS conf_log;
DROP TABLE IF EXISTS conf_msg;
DROP TABLE IF EXISTS env;
DROP TABLE IF EXISTS project;
DROP TABLE IF EXISTS user;

CREATE TABLE `conf`
(
    `app_name`    VARCHAR(100) NOT NULL
        COMMENT '所属项目AppName',
    `env`         VARCHAR(100) NOT NULL
        COMMENT 'Env',
    `k` VARCHAR(200) NOT NULL
        COMMENT '配置Key',
    `description` VARCHAR(100) NOT NULL
        COMMENT '配置描述',
    `v` VARCHAR(2000) DEFAULT NULL
        COMMENT '配置Value',
    PRIMARY KEY (`app_name`, `env`, `k`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `conf_log`
(
    `id`           INT(11)      NOT NULL AUTO_INCREMENT,
    `app_name`     VARCHAR(100) NOT NULL
        COMMENT '所属项目AppName',
    `description`  VARCHAR(100) NOT NULL
        COMMENT '配置描述',
    `env`          VARCHAR(100) NOT NULL
        COMMENT 'Env',
    `k` VARCHAR(200) NOT NULL
        COMMENT '配置Key',
    `log_time`     BIGINT(11)   NOT NULL
        COMMENT '操作时间',
    `operate_type` TINYINT(2)   NOT NULL
        COMMENT '操作类型',
    `operator`     VARCHAR(100) NOT NULL
  COMMENT '操作人',
    `v` VARCHAR(2000) DEFAULT NULL
        COMMENT '配置Value',
    PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `conf_msg`
(
    `id`          INT(11)      NOT NULL AUTO_INCREMENT,
    `app_name`    VARCHAR(100) NOT NULL
        COMMENT '所属项目AppName',
    `change_time` BIGINT(11)   NOT NULL,
    `env`         VARCHAR(100) NOT NULL
        COMMENT 'Env',
    `k` VARCHAR(200) NOT NULL
        COMMENT '配置Key',
    `v` VARCHAR(2000) DEFAULT NULL
        COMMENT '配置Value',
    PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `env`
(
    `env`         VARCHAR(100) NOT NULL
        COMMENT 'Env',
    `description` VARCHAR(100) NOT NULL
        COMMENT '环境描述',
    `torder`      TINYINT(4)   NOT NULL DEFAULT '0'
        COMMENT '显示排序',
    PRIMARY KEY (`env`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8;

CREATE TABLE `project`
(
    `app_name` VARCHAR(100) NOT NULL
        COMMENT 'AppName',
    `title`    VARCHAR(100) NOT NULL
        COMMENT '项目名称',
    PRIMARY KEY (`app_name`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8;

CREATE TABLE `User` (
                        `account`        VARCHAR(100) NOT NULL
  COMMENT '账号',
                        `name`           VARCHAR(100) NOT NULL
  COMMENT '用户名',
                        `password`       VARCHAR(100) NOT NULL
  COMMENT '密码',
                        `permission`     TINYINT(4)   NOT NULL DEFAULT '0'
  COMMENT '权限：0-普通用户、1-管理员',
                        `permission_data` VARCHAR(1000) DEFAULT NULL
                            COMMENT '权限配置数据',
                        PRIMARY KEY (account)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
