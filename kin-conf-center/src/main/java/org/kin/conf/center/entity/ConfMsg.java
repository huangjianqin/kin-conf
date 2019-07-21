package org.kin.conf.center.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author huangjianqin
 * @date 2019/7/12
 */
@Entity
@Table(name = "ConfMsg")
public class ConfMsg implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "int(11) NOT NULL AUTO_INCREMENT")
    private int id;
    @Column(columnDefinition = "bigint(11) NOT NULL")
    private long changeTime;
    @Column(columnDefinition = "varchar(100) NOT NULL COMMENT '所属项目AppName'")
    private String appName;
    @Column(columnDefinition = "varchar(100) NOT NULL COMMENT 'Env'")
    private String env;
    @Column(columnDefinition = "varchar(200) NOT NULL COMMENT '配置Key'")
    private String keyV;
    @Column(columnDefinition = "varchar(2000) DEFAULT NULL COMMENT '配置Value'")
    private String value;

    //setter && getter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(long changeTime) {
        this.changeTime = changeTime;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
