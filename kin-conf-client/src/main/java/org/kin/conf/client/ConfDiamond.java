package org.kin.conf.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.kin.conf.client.domain.ConfDTO;
import org.kin.framework.utils.StringUtils;
import org.kin.transport.http.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author huangjianqin
 * @date 2019/7/8
 */
class ConfDiamond {
    private static final Logger log = LoggerFactory.getLogger(ConfDiamond.class);

    private static final String GET_URL = "/conf/get";
    private static final String MONITOR_URL = "/conf/monitor";

    private static final String REQ_APPNAME = "appName";
    private static final String REQ_ENV = "env";
    private static final String REQ_KEYS = "keys";

    private static final String RESP_RESULT = "code";
    private static final int RESP_SUCCESS_RESULT = 200;
    private static final String RESP_DATA = "data";
    private static final String RESP_MSG = "msg";

    static ConfDTO get(String key) {
        Map<String, String> data = get(Collections.singletonList(key));
        if (data.containsKey(key)) {
            return new ConfDTO(key, data.get(key));
        }
        return null;
    }

    static Map<String, String> get(Collection<String> keys) {
        for (String diamondAddress : KinConf.getDiamondAddresses()) {
            String requestUrl = diamondAddress + GET_URL;
            Map<String, Object> params = new HashMap<>();
            params.put(REQ_APPNAME, KinConf.getAppName());
            params.put(REQ_ENV, KinConf.getEnv());
            params.put(REQ_KEYS, keys);

            String respJson = HttpUtils.post(requestUrl, params).getContent();
            if (StringUtils.isNotBlank(respJson)) {
                JSONObject jsonObject = JSON.parseObject(respJson);
                int result = jsonObject.getInteger(RESP_RESULT);
                if (result == RESP_SUCCESS_RESULT && jsonObject.containsKey(RESP_DATA)) {
                    return jsonObject.getObject(RESP_DATA, Map.class);
                } else {
                    log.error("请求'{}'异常, 返回{}", requestUrl, respJson);
                }
            }
        }
        return Collections.emptyMap();
    }

    static boolean monitor(Collection<String> keys) {
        for (String diamondAddress : KinConf.getDiamondAddresses()) {
            String requestUrl = diamondAddress + MONITOR_URL;
            Map<String, Object> params = new HashMap<>();
            params.put(REQ_APPNAME, KinConf.getAppName());
            params.put(REQ_ENV, KinConf.getEnv());
            params.put(REQ_KEYS, keys);

            String respJson = HttpUtils.post(requestUrl, params).getContent();
            if (StringUtils.isNotBlank(respJson)) {
                JSONObject jsonObject = JSON.parseObject(respJson);
                int result = jsonObject.getInteger(RESP_RESULT);
                if (result == RESP_SUCCESS_RESULT) {
                    return true;
                }
            }
        }

        return false;
    }
}
