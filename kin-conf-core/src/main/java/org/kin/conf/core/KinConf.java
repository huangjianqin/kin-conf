package org.kin.conf.core;

import com.google.common.base.Preconditions;
import org.kin.conf.core.domain.ConfDTO;
import org.kin.conf.core.exception.ConfNotExistException;
import org.kin.framework.actor.Keeper;
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
    private static List<String> centerAddresses = new ArrayList<>();
    private static String env;
    private static String mirrorFile;

    static {
        Keeper.keep(() -> {
            try {
                refreshCacheAndMirror();
            } catch (Exception e) {
                ExceptionUtils.log(e);
            }
        });
    }

    public static synchronized void init(String appName, String centerAddress, String env, String mirrorFile) {
        Preconditions.checkArgument(StringUtils.isNotBlank(appName), "param appName must not blank");
        Preconditions.checkArgument(StringUtils.isNotBlank(centerAddress), "param centerAddress must not blank");
        Preconditions.checkArgument(StringUtils.isNotBlank(env), "param env must not blank");
        Preconditions.checkArgument(StringUtils.isNotBlank(mirrorFile), "param mirrorFile must not blank");

        KinConf.appName = appName;
        KinConf.centerAddresses.addAll(Arrays.asList(centerAddress.split(",")));
        KinConf.env = env;
        boolean needRefreshCache = false;
        if (StringUtils.isNotBlank(KinConf.mirrorFile) && !KinConf.mirrorFile.equals(mirrorFile)) {
            needRefreshCache = true;
        }
        KinConf.mirrorFile = mirrorFile;
        if (needRefreshCache) {
            ConfCache.init();
        }
    }

    private static void refreshCacheAndMirror() throws InterruptedException {
        if (ConfCache.cacheSize() == 0) {
            TimeUnit.SECONDS.sleep(3);
            return;
        }

        // monitor
        Set<String> keys = ConfCache.cachedKeys();

        boolean monitorReturn = ConfCenter.monitor(keys);
        // avoid fail-retry request too quick
        if (!monitorReturn) {
            TimeUnit.SECONDS.sleep(10);
        }

        // refresh cache: center > cache
        if (keys.size() > 0) {
            Map<String, String> centerConfs = ConfCenter.get(keys);
            if (centerConfs != null && centerConfs.size() > 0) {
                for (String key : centerConfs.keySet()) {
                    String value = centerConfs.get(key);

                    ConfDTO conf = ConfCache.get(key);
                    if (conf != null && conf.getValue() != null && conf.getValue().equals(value)) {
                        //配置没有发生变化
                    } else {
                        //配置变化, 并触发listener
                        ConfCache.put(key, value);

                        ConfListenerManager.onConfChange(key, value);
                    }

                }
            }
        }

        // refresh mirror: cache > mirror
        Map<String, String> mirrorConfs = new HashMap<>();
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

    public static List<String> getCenterAddresses() {
        return centerAddresses;
    }

    public static String getEnv() {
        return env;
    }

    public static String getMirrorFile() {
        return mirrorFile;
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

        //level 3: center
        conf = ConfCenter.get(key);
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
        return Boolean.valueOf(value);
    }

    public static short getShort(String key) {
        String value = get(key, null);
        if (value == null) {
            throw new ConfNotExistException(key);
        }
        return Short.valueOf(value);
    }

    public static int getInt(String key) {
        String value = get(key, null);
        if (value == null) {
            throw new ConfNotExistException(key);
        }
        return Integer.valueOf(value);
    }

    public static long getLong(String key) {
        String value = get(key, null);
        if (value == null) {
            throw new ConfNotExistException(key);
        }
        return Long.valueOf(value);
    }

    public static float getFloat(String key) {
        String value = get(key, null);
        if (value == null) {
            throw new ConfNotExistException(key);
        }
        return Float.valueOf(value);
    }

    public static double getDouble(String key) {
        String value = get(key, null);
        if (value == null) {
            throw new ConfNotExistException(key);
        }
        return Double.valueOf(value);
    }
}
