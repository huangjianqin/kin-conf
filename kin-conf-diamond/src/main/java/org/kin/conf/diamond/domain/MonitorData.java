package org.kin.conf.diamond.domain;

import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huangjianqin
 * @date 2019/7/17
 */
public class MonitorData {
    private MonitorData() {

    }

    private static Map<String, List<DeferredResult<WebResponse<String>>>> deferredResultMap = new ConcurrentHashMap<>();


    private static String key(String appName, String env, String key) {
        return appName + env + key;
    }

    public static void add(String appName, String env, String key, DeferredResult<WebResponse<String>> deferredResult) {
        String mapKey = key(appName, env, key);
        List<DeferredResult<WebResponse<String>>> list = deferredResultMap.get(mapKey);
        if (list == null) {
            list = new ArrayList<>();
        }

        list.add(deferredResult);
        deferredResultMap.put(key, list);
    }

    public static List<DeferredResult<WebResponse<String>>> get(String appName, String env, String key) {
        String mapKey = key(appName, env, key);
        return deferredResultMap.get(mapKey);
    }

    public static List<DeferredResult<WebResponse<String>>> remove(String appName, String env, String key) {
        String mapKey = key(appName, env, key);
        return deferredResultMap.remove(mapKey);
    }
}
