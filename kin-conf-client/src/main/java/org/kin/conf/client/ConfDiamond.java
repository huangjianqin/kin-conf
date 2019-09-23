package org.kin.conf.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.kin.conf.client.domain.ConfDTO;
import org.kin.framework.JvmCloseCleaner;
import org.kin.framework.concurrent.ThreadManager;
import org.kin.framework.utils.ExceptionUtils;
import org.kin.framework.utils.StringUtils;
import org.kin.transport.http.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author huangjianqin
 * @date 2019/7/8
 */
class ConfDiamond {
    private static final Logger log = LoggerFactory.getLogger(ConfDiamond.class);

    private static final String GET_URL = "/conf/find";
    private static final String MONITOR_URL = "/conf/monitor";

    private static final String REQ_APPNAME = "appName";
    private static final String REQ_ENV = "env";
    private static final String REQ_KEYS = "keys";

    private static final String RESP_RESULT = "code";
    private static final int RESP_SUCCESS_RESULT = 200;
    private static final String RESP_DATA = "data";
    private static final String RESP_MSG = "msg";

    /** 合并http请求相关 */
    private static Object lock = new Object();
    private static volatile HashSet<String> keyPool = new HashSet<>();
    private static Future<Map<String, String>> future = null;
    private static ThreadManager executor = new ThreadManager(new ScheduledThreadPoolExecutor(1));

    static {
        JvmCloseCleaner.DEFAULT().add(() -> executor.shutdown());
    }

    /**
     * 合并http请求配置数据
     */
    static ConfDTO mergeGet(String key) {
        Future<Map<String, String>> future = null;
        synchronized (lock) {
            keyPool.add(key);
            if (ConfDiamond.future == null) {
                ConfDiamond.future = executor.schedule(() -> {
                    Set<String> reqKeys = new HashSet<>();
                    synchronized (lock) {
                        ConfDiamond.future = null;
                        reqKeys.addAll(ConfDiamond.keyPool);
                        ConfDiamond.keyPool = new HashSet<>();
                    }
                    return get(reqKeys);
                }, 200, TimeUnit.MILLISECONDS);
            }
            future = ConfDiamond.future;
            while (future != null && !future.isDone()) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    ExceptionUtils.log(e);
                }
            }
            try {
                Map<String, String> data = future.get();
                return new ConfDTO(key, data.get(key));
            } catch (InterruptedException | ExecutionException e) {
                ExceptionUtils.log(e);
            }
        }

        return null;
    }

    static ConfDTO get(String key) {
        if (KinConf.isMerge()) {
            return mergeGet(key);
        } else {
            Map<String, String> data = get(Collections.singletonList(key));
            if (data.containsKey(key)) {
                return new ConfDTO(key, data.get(key));
            }
            return null;
        }
    }

    static Map<String, String> get(Collection<String> keys) {
        for (String diamondAddress : KinConf.getDiamondAddresses()) {
            String requestUrl = diamondAddress + GET_URL;
            Map<String, Object> params = new HashMap<>();
            params.put(REQ_APPNAME, KinConf.getAppName());
            params.put(REQ_ENV, KinConf.getEnv());
            params.put(REQ_KEYS, new ArrayList<>(keys));

            String respJson = HttpUtils.postJson(requestUrl, params).getContent();
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

            String respJson = HttpUtils.postJson(requestUrl, params).getContent();
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
