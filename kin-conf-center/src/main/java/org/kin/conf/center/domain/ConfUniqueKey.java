package org.kin.conf.center.domain;

import java.io.Serializable;

/**
 * @author huangjianqin
 * @date 2019/7/13
 */
public class ConfUniqueKey implements Serializable {
    private String appName;
    private String env;
    private String keyV;

    public ConfUniqueKey() {
    }

    public ConfUniqueKey(String appName, String env, String keyV) {
        this.appName = appName;
        this.env = env;
        this.keyV = keyV;
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

    public String getKeyV() {
        return keyV;
    }

    public void setKeyV(String keyV) {
        this.keyV = keyV;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConfUniqueKey that = (ConfUniqueKey) o;

        if (!appName.equals(that.appName)) return false;
        if (!env.equals(that.env)) return false;
        return keyV.equals(that.keyV);
    }

    @Override
    public int hashCode() {
        int result = appName.hashCode();
        result = 31 * result + env.hashCode();
        result = 31 * result + keyV.hashCode();
        return result;
    }
}
