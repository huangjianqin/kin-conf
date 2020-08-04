package org.kin.conf.client.starter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author huangjianqin
 * @date 2020/8/4
 */
@ConfigurationProperties("kin.conf.client")
public class KinConfClientProperties {
    private String diamondAddress = "http://localhost:8080";
    private String env = "test";
    private String mirrorFile = "data/mirror.properties";
    private boolean enable = true;

    //setter && getter

    public String getDiamondAddress() {
        return diamondAddress;
    }

    public void setDiamondAddress(String diamondAddress) {
        this.diamondAddress = diamondAddress;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getMirrorFile() {
        return mirrorFile;
    }

    public void setMirrorFile(String mirrorFile) {
        this.mirrorFile = mirrorFile;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}