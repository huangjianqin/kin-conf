package org.kin.conf.client.domain;

/**
 * @author huangjianqin
 * @date 2019/7/8
 */
public class ConfDTO {
    private String key;
    private String value;

    public ConfDTO(String key) {
        this.key = key;
    }

    public ConfDTO(String key, String value) {
        this.key = key.trim();
        this.value = value.trim();
    }

    //setter && getter
    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
