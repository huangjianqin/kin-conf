package org.kin.conf.client;

import org.kin.conf.client.domain.ConfDTO;
import org.kin.conf.client.domain.ServerResponse;
import org.kin.conf.client.utils.HttpUtils;
import org.kin.framework.JvmCloseCleaner;
import org.kin.framework.concurrent.ThreadManager;
import org.kin.framework.utils.ExceptionUtils;
import org.kin.framework.utils.JSON;
import org.kin.framework.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
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
    private static ThreadManager EXECUTOR =
            ThreadManager.fix(5, "diamond-merge-request-", 5, "diamond-merge-request-schedule-");

    static {
        JvmCloseCleaner.DEFAULT().add(() -> EXECUTOR.shutdown());
    }

    /**
     * 合并http请求配置数据
     */
    static ConfDTO mergeGet(String key) {
        Future<Map<String, String>> future;
        synchronized (lock) {
            keyPool.add(key);
            if (ConfDiamond.future == null) {
                ConfDiamond.future = EXECUTOR.schedule(() -> {
                    Set<String> reqKeys;
                    synchronized (lock) {
                        ConfDiamond.future = null;
                        reqKeys = new HashSet<>(ConfDiamond.keyPool);
                        ConfDiamond.keyPool = new HashSet<>();
                    }
                    return get(reqKeys);
                }, 300, TimeUnit.MILLISECONDS);
            }
            future = ConfDiamond.future;
        }
        while (!future.isDone()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                ExceptionUtils.log(e);
            }
        }
        try {
            Map<String, String> data = future.get();
            if (Objects.nonNull(data.get(key))) {
                return new ConfDTO(key, data.get(key));
            }
        } catch (InterruptedException | ExecutionException e) {
            ExceptionUtils.log(e);
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
            Map<String, Object> params = new HashMap<>(10);
            params.put(REQ_APPNAME, KinConf.getAppName());
            params.put(REQ_ENV, KinConf.getEnv());
            params.put(REQ_KEYS, new ArrayList<>(keys));

            try {
                String respJson = HttpUtils.post(requestUrl, params);
                if (StringUtils.isNotBlank(respJson)) {
                    ServerResponse response = JSON.read(respJson, ServerResponse.class);
                    if (response.getCode() == RESP_SUCCESS_RESULT) {
                        return response.getData();
                    } else {
                        log.error("请求'{}'异常, 返回{}", requestUrl, respJson);
                    }
                }
            } catch (Exception e) {
                ExceptionUtils.log(e);
            }
        }
        return Collections.emptyMap();
    }

    static boolean monitor(Collection<String> keys) {
        for (String diamondAddress : KinConf.getDiamondAddresses()) {
            String requestUrl = diamondAddress + MONITOR_URL;
            Map<String, Object> params = new HashMap<>(10);
            params.put(REQ_APPNAME, KinConf.getAppName());
            params.put(REQ_ENV, KinConf.getEnv());
            params.put(REQ_KEYS, keys);

            try {
                String respJson = HttpUtils.post(requestUrl, params);
                if (StringUtils.isNotBlank(respJson)) {
                    ServerResponse response = JSON.read(respJson, ServerResponse.class);
                    if (response.getCode() == RESP_SUCCESS_RESULT) {
                        return true;
                    }
                }
            } catch (Exception e) {
                ExceptionUtils.log(e);
            }
        }

        return false;
    }
}
