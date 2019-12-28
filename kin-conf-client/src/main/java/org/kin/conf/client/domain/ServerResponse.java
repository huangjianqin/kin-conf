package org.kin.conf.client.domain;

import java.util.Collections;
import java.util.Map;

/**
 * @author huangjianqin
 * @date 2019-12-28
 */
public class ServerResponse {
    private int code;
    private Map<String, String> data = Collections.emptyMap();

    //setter && getter

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
