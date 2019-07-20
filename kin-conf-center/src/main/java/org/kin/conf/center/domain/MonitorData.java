package org.kin.conf.center.domain;

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

    private static Map<String, List<DeferredResult<CommonResponse<String>>>> deferredResultMap = new ConcurrentHashMap<>();


    private static String key(String appName, String env, String key) {
        return appName + env + key;
    }

    public static void add(String appName, String env, String key, DeferredResult<CommonResponse<String>> deferredResult) {
        String mapKey = key(appName, env, key);
        List<DeferredResult<CommonResponse<String>>> list = deferredResultMap.get(mapKey);
        if (list == null) {
            list = new ArrayList<>();
        }

        list.add(deferredResult);
        deferredResultMap.put(key, list);
    }

    public static List<DeferredResult<CommonResponse<String>>> get(String appName, String env, String key) {
        String mapKey = key(appName, env, key);
        return deferredResultMap.get(mapKey);
    }

    public static List<DeferredResult<CommonResponse<String>>> remove(String appName, String env, String key) {
        String mapKey = key(appName, env, key);
        return deferredResultMap.remove(mapKey);
    }
}
