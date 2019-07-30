package org.kin.conf.diamond.entity;


import org.kin.conf.diamond.domain.ConfUniqueKey;

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
    private String keyV;
    @Id
    @Column(columnDefinition = "varchar(100) NOT NULL COMMENT '所属项目AppName'")
    private String appName;
    @Column(columnDefinition = "varchar(100) NOT NULL COMMENT '配置描述'")
    private String description;
    @Column(columnDefinition = "varchar(2000) DEFAULT NULL COMMENT '配置Value'")
    private String value;

    //setter && getter
    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getKeyV() {
        return keyV;
    }

    public void setKeyV(String keyV) {
        this.keyV = keyV;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
