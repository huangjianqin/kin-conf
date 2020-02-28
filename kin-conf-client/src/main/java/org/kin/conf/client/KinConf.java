package org.kin.conf.client;

import com.google.common.base.Preconditions;
import org.kin.conf.client.domain.ConfDTO;
import org.kin.conf.client.exception.ConfNotExistException;
import org.kin.framework.concurrent.Keeper;
import org.kin.framework.utils.CollectionUtils;
import org.kin.framework.utils.ExceptionUtils;
import org.kin.framework.utils.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author huangjianqin
 * @date 2019/7/8
 */
public class KinConf {
    private static String appName;
    private static List<String> diamondAddresses = new ArrayList<>();
    private static String env;
    private static String mirrorFile;
    /** 标识是否合并http请求 */
    private static boolean merge;

    static {
        Keeper.keep(() -> {
            try {
                refreshCacheAndMirror();
            } catch (Exception e) {
                ExceptionUtils.log(e);
            }
        });
    }

    public static synchronized void init(String mirrorFile) {
        init("local", "", "local", mirrorFile);
    }

    public static synchronized void init(String appName, String diamondAddress, String env, String mirrorFile) {
        init(appName, diamondAddress, env, mirrorFile, false);
    }

    public static synchronized void init(String appName, String diamondAddress, String env, String mirrorFile, boolean merge) {
        Preconditions.checkArgument(StringUtils.isNotBlank(mirrorFile), "param mirrorFile must not blank");

        KinConf.appName = appName;
        if (StringUtils.isNotBlank(diamondAddress)) {
            KinConf.diamondAddresses.addAll(Arrays.asList(diamondAddress.split(",")));
        }
        KinConf.env = env;
        boolean needRefreshCache = false;
        if (!mirrorFile.equals(KinConf.mirrorFile)) {
            needRefreshCache = true;
        }
        KinConf.mirrorFile = mirrorFile;
        KinConf.merge = merge;
        if (needRefreshCache) {
            ConfCache.init();
        }
    }

    private static void refreshCacheAndMirror() throws InterruptedException {
        if (CollectionUtils.isEmpty(KinConf.getDiamondAddresses())) {
            return;
        }

        if (ConfCache.cacheSize() == 0) {
            TimeUnit.SECONDS.sleep(3);
            return;
        }

        // monitor
        Set<String> keys = ConfCache.cachedKeys();
        //异步http请求, 会根据server返回的DeferredResult决定阻塞时间, 如果在等待期间配置发生, server会立即返回
        if (!ConfDiamond.monitor(keys)) {
            TimeUnit.SECONDS.sleep(10);
        }

        // refresh cache: diamond > cache
        if (keys.size() > 0) {
            Map<String, String> diamondConfs = ConfDiamond.get(keys);
            if (diamondConfs != null && diamondConfs.size() > 0) {
                for (String key : diamondConfs.keySet()) {
                    String value = diamondConfs.get(key);

                    ConfDTO conf = ConfCache.get(key);
                    if (conf == null || conf.getValue() == null || !conf.getValue().equals(value)) {
                        //配置变化, 并触发listener
                        ConfCache.put(key, value);

                        ConfListenerManager.onConfChange(key, value);
                    }
                }
            }
        }

        // refresh mirror: cache > mirror
        Map<String, String> mirrorConfs = new HashMap<>(keys.size());
        for (String key : keys) {
            ConfDTO conf = ConfCache.get(key);
            mirrorConfs.put(key, conf.getValue() != null ? conf.getValue() : "");
        }

        ConfMirror.writeMirror(mirrorConfs);
    }

    //-----------------------------------------------------------------------------------------------------------

    public static String getAppName() {
        return appName;
    }

    public static List<String> getDiamondAddresses() {
        return diamondAddresses;
    }

    public static String getEnv() {
        return env;
    }

    public static String getMirrorFile() {
        return mirrorFile;
    }

    public static boolean isMerge() {
        return merge;
    }

    //--------------------------------------------------api------------------------------------------------------

    public static String get(String key, String defaultVal) {
        //level 1: cache
        ConfDTO conf = ConfCache.get(key);
        if (conf != null) {
            return conf.getValue();
        }

        //level 2: mirror
        conf = ConfMirror.get(key);
        if (conf != null) {
            //写回缓存
            ConfCache.put(conf.getKey(), conf.getValue());
            return conf.getValue();
        }

        //level 3: diamond
        conf = ConfDiamond.get(key);
        if (conf != null) {
            //写回缓存
            ConfCache.put(conf.getKey(), conf.getValue());
            return conf.getValue();
        }

        return defaultVal;
    }

    public static String get(String key) {
        return get(key, null);
    }

    public static boolean getBoolean(String key) {
        String value = get(key, null);
        if (value == null) {
            throw new ConfNotExistException(key);
        }
        return Boolean.parseBoolean(value);
    }

    public static short getShort(String key) {
        String value = get(key, null);
        if (value == null) {
            throw new ConfNotExistException(key);
        }
        return Short.parseShort(value);
    }

    public static int getInt(String key) {
        String value = get(key, null);
        if (value == null) {
            throw new ConfNotExistException(key);
        }
        return Integer.parseInt(value);
    }

    public static long getLong(String key) {
        String value = get(key, null);
        if (value == null) {
            throw new ConfNotExistException(key);
        }
        return Long.parseLong(value);
    }

    public static float getFloat(String key) {
        String value = get(key, null);
        if (value == null) {
            throw new ConfNotExistException(key);
        }
        return Float.parseFloat(value);
    }

    public static double getDouble(String key) {
        String value = get(key, null);
        if (value == null) {
            throw new ConfNotExistException(key);
        }
        return Double.parseDouble(value);
    }
}
