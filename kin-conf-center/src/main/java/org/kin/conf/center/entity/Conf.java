package org.kin.conf.center.entity;


import org.kin.conf.center.domain.ConfUniqueKey;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author huangjianqin
 * @date 2019/7/12
 */
@Entity
@Table(name = "Conf")
@IdClass(ConfUniqueKey.class)
public class Conf implements Serializable {
    @Id
    @Column(columnDefinition = "varchar(100) NOT NULL COMMENT 'Env'")
    private String env;
    @Id
    @Column(columnDefinition = "varchar(200) NOT NULL COMMENT '配置Key'")
    private String key;
    @Column(columnDefinition = "varchar(100) NOT NULL COMMENT '所属项目AppName'")
    private String appName;
    @Column(columnDefinition = "varchar(100) NOT NULL COMMENT '配置描述'")
    private String desc;
    @Column(columnDefinition = "varchar(2000) DEFAULT NULL COMMENT '配置Value'")
    private String value;

    //setter && getter
    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
