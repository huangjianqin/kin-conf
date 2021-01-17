package org.kin.conf.diamond.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * @author huangjianqin
 * @date 2019/7/12
 */
@TableName(autoResultMap = true)
public class ConfMsg implements Serializable {
    /** id */
    private int id;
    /** 配置变化时间 */
    private long changeTime;
    /** 所属项目AppName */
    private String appName;
    /** 项目应用环境 */
    private String env;
    /** 配置Key */
    @TableField(value = "k")
    private String key;
    /** 配置Value */
    @TableField(value = "v")
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
