package org.kin.conf.center.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author huangjianqin
 * @date 2019/7/12
 */
@Entity
@Table(name = "ConfLog")
public class ConfLog implements Serializable {
    private static final int ADD_OPR = 1;
    private static final int UPDATE_OPR = 2;
    private static final int DELETE_OPR = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "int(11) NOT NULL AUTO_INCREMENT")
    private int id;
    @Column(columnDefinition = "varchar(100) NOT NULL COMMENT 'Env'")
    private String env;
    @Column(columnDefinition = "varchar(200) NOT NULL COMMENT '配置Key'")
    private String keyV;
    @Column(columnDefinition = "varchar(100) NOT NULL COMMENT '所属项目AppName'")
    private String appName;
    @Column(columnDefinition = "varchar(100) NOT NULL COMMENT '配置描述'")
    private String description;
    @Column(columnDefinition = "varchar(2000) DEFAULT NULL COMMENT '配置Value'")
    private String value;
    @Column(columnDefinition = "bigint(11) NOT NULL COMMENT '操作时间'")
    private long logTime;
    @Column(columnDefinition = "varchar(100) NOT NULL COMMENT '操作人'")
    private String operator;
    @Column(columnDefinition = "tinyint(2) NOT NULL COMMENT '操作类型'")
    private int operateType;

    public ConfLog() {
    }

    public ConfLog(String env, String keyV, String appName, String description, String value, long logTime, String operator, int operateType) {
        this.env = env;
        this.keyV = keyV;
        this.appName = appName;
        this.description = description;
        this.value = value;
        this.logTime = logTime;
        this.operator = operator;
        this.operateType = operateType;
    }

    public static ConfLog logAdd(String env, String key, String appName, String desc, String value, String operator) {
        return new ConfLog(env, key, appName, desc, value, System.currentTimeMillis(), operator, ADD_OPR);
    }

    public static ConfLog logUpdate(String env, String key, String appName, String desc, String value, String operator) {
        return new ConfLog(env, key, appName, desc, value, System.currentTimeMillis(), operator, UPDATE_OPR);
    }

    public static ConfLog logDelete(String env, String key, String appName, String desc, String value, String operator) {
        return new ConfLog(env, key, appName, desc, value, System.currentTimeMillis(), operator, DELETE_OPR);
    }

    //setter && getter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public long getLogTime() {
        return logTime;
    }

    public void setLogTime(long logTime) {
        this.logTime = logTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public int getOperateType() {
        return operateType;
    }

    public void setOperateType(int operateType) {
        this.operateType = operateType;
    }
}
