DROP TABLE IF EXISTS Conf;
DROP TABLE IF EXISTS ConfLog;
DROP TABLE IF EXISTS ConfMsg;
DROP TABLE IF EXISTS Env;
DROP TABLE IF EXISTS hibernate_sequence;
DROP TABLE IF EXISTS Project;
DROP TABLE IF EXISTS User;

CREATE TABLE `Conf` (
  `appName`     VARCHAR(100) NOT NULL
  COMMENT '所属项目AppName',
  `env`         VARCHAR(100) NOT NULL
  COMMENT 'Env',
  `keyV`        VARCHAR(200) NOT NULL
  COMMENT '配置Key',
  `description` VARCHAR(100) NOT NULL
  COMMENT '配置描述',
  `value`       VARCHAR(2000) DEFAULT NULL
  COMMENT '配置Value',
  PRIMARY KEY (`appName`, `env`, `keyV`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `ConfLog` (
  `id`          INT(11)      NOT NULL AUTO_INCREMENT,
  `appName`     VARCHAR(100) NOT NULL
  COMMENT '所属项目AppName',
  `description` VARCHAR(100) NOT NULL
  COMMENT '配置描述',
  `env`         VARCHAR(100) NOT NULL
  COMMENT 'Env',
  `keyV`        VARCHAR(200) NOT NULL
  COMMENT '配置Key',
  `logTime`     BIGINT(11)   NOT NULL
  COMMENT '操作时间',
  `operateType` TINYINT(2)   NOT NULL
  COMMENT '操作类型',
  `operator`    VARCHAR(100) NOT NULL
  COMMENT '操作人',
  `value`       VARCHAR(2000)         DEFAULT NULL
  COMMENT '配置Value',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `ConfMsg` (
  `id`         INT(11)      NOT NULL AUTO_INCREMENT,
  `appName`    VARCHAR(100) NOT NULL
  COMMENT '所属项目AppName',
  `changeTime` BIGINT(11)   NOT NULL,
  `env`        VARCHAR(100) NOT NULL
  COMMENT 'Env',
  `keyV`       VARCHAR(200) NOT NULL
  COMMENT '配置Key',
  `value`      VARCHAR(2000)         DEFAULT NULL
  COMMENT '配置Value',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `Env` (
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

CREATE TABLE `hibernate_sequence` (
  `next_val` BIGINT
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `Project` (
  `appName` VARCHAR(100) NOT NULL
  COMMENT 'AppName',
  `title`   VARCHAR(100) NOT NULL
  COMMENT '项目名称',
  PRIMARY KEY (`appName`)
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
  `permissionData` VARCHAR(1000)         DEFAULT NULL
  COMMENT '权限配置数据',
  PRIMARY KEY (account)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
