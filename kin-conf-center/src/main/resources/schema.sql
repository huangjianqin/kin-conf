DROP TABLE IF EXISTS Conf;
DROP TABLE IF EXISTS ConfLog;
DROP TABLE IF EXISTS ConfMsg;
DROP TABLE IF EXISTS Env;
DROP TABLE IF EXISTS hibernate_sequence;
DROP TABLE IF EXISTS Project;
DROP TABLE IF EXISTS User;

CREATE TABLE `Conf` (
  `appName` varchar(100) NOT NULL COMMENT '所属项目AppName',
  `env` varchar(100) NOT NULL COMMENT 'Env',
  `keyV` varchar(200) NOT NULL COMMENT '配置Key',
  `description` varchar(100) NOT NULL COMMENT '配置描述',
  `value` varchar(2000) DEFAULT NULL COMMENT '配置Value',
  PRIMARY KEY (`appName`, `env`, `keyV`)
) engine=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `ConfLog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `appName` varchar(100) NOT NULL COMMENT '所属项目AppName',
  `description` varchar(100) NOT NULL COMMENT '配置描述',
  `env` varchar(100) NOT NULL COMMENT 'Env',
  `keyV` varchar(200) NOT NULL COMMENT '配置Key',
  `logTime` bigint(11) NOT NULL COMMENT '操作时间',
  `operateType` tinyint(2) NOT NULL COMMENT '操作类型',
  `operator` varchar(100) NOT NULL COMMENT '操作人',
  `value` varchar(2000) DEFAULT NULL COMMENT '配置Value',
  PRIMARY KEY (`id`)
) engine=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `ConfMsg` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `appName` varchar(100) NOT NULL COMMENT '所属项目AppName',
  `changeTime` bigint(11) NOT NULL,
  `env` varchar(100) NOT NULL COMMENT 'Env',
  `keyV` varchar(200) NOT NULL COMMENT '配置Key',
  `value` varchar(2000) DEFAULT NULL COMMENT '配置Value',
  PRIMARY KEY (`id`)
) engine=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `Env` (
  `env` varchar(100) NOT NULL COMMENT 'Env',
  `description` varchar(100) NOT NULL COMMENT '环境描述',
  `torder` tinyint(4) NOT NULL DEFAULT '0' COMMENT '显示排序',
  PRIMARY KEY (`env`)
) engine=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint
) engine=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `Project` (
  `appName` varchar(100) NOT NULL COMMENT 'AppName',
  `title` varchar(100) NOT NULL COMMENT '项目名称',
  PRIMARY KEY (`appName`)
) engine=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `User` (
  `account` varchar(100) NOT NULL COMMENT '账号',
  `name` varchar(100) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `permission` tinyint(4) NOT NULL DEFAULT '0' COMMENT '权限：0-普通用户、1-管理员',
  `permissionData` varchar(1000) DEFAULT NULL COMMENT '权限配置数据',
  PRIMARY KEY  (account)
) engine=InnoDB DEFAULT CHARSET=utf8;
