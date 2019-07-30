package org.kin.conf.diamond.domain;

import java.util.ArrayList;

/**
 * @author huangjianqin
 * @date 2019/7/21
 */
public class FindConfParams {
    private String appName;
    private String env;
    private ArrayList<String> keys;

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

    public ArrayList<String> getKeys() {
        return keys;
    }

    public void setKeys(ArrayList<String> keys) {
        this.keys = keys;
    }
}
