package org.kin.conf.center.domain;

import java.io.Serializable;

/**
 * @author huangjianqin
 * @date 2019/7/13
 */
public class ConfUniqueKey implements Serializable {
    private String appName;
    private String env;
    private String key;

    public ConfUniqueKey() {
    }

    public ConfUniqueKey(String appName, String env, String key) {
        this.appName = appName;
        this.env = env;
        this.key = key;
    }

    //setter && getter
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
}
