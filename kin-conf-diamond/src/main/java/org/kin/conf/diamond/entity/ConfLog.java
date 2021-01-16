package org.kin.conf.diamond.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * @author huangjianqin
 * @date 2019/7/12
 */
@TableName(autoResultMap = true)
public class ConfLog implements Serializable {
    private static final int ADD_OPR = 1;
    private static final int UPDATE_OPR = 2;
    private static final int DELETE_OPR = 3;

    /** id */
    private int id;
    /** 项目应用环境 */
    private String env;
    /** 配置Key */
    private String key;
    /** 所属项目AppName */
    private String appName;
    /** 配置描述 */
    private String description;
    /** 配置Value */
    private String value;
    /** 操作时间 */
    private long logTime;
    /** 操作人 */
    private String operator;
    /** 操作类型 */
    private int operateType;

    public ConfLog() {
    }

    public ConfLog(String env, String key, String appName, String description, String value, long logTime, String operator, int operateType) {
        this.env = env;
        this.key = key;
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
