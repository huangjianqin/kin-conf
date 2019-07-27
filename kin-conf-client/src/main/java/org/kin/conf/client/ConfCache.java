package org.kin.conf.client;

import org.kin.conf.client.domain.ConfDTO;
import org.kin.framework.utils.CollectionUtils;
import org.kin.framework.utils.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huangjianqin
 * @date 2019/7/8
 */
class ConfCache {
    private static Map<String, ConfDTO> CACHE = new ConcurrentHashMap();

    static synchronized void init() {
        CACHE.clear();

        Map<String, String> initConfs = new HashMap<>();
        //从mirror读取配置
        Map<String, String> mirrorConfs = ConfMirror.mirrorConfs();

        //从center更新配置
        Map<String, String> centerConfs = Collections.emptyMap();
        if (CollectionUtils.isNonEmpty(mirrorConfs.keySet())) {
            centerConfs = ConfCenter.get(mirrorConfs.keySet());
        }

        if (CollectionUtils.isNonEmpty(mirrorConfs)) {
            initConfs.putAll(mirrorConfs);
        }
        if (CollectionUtils.isNonEmpty(centerConfs)) {
            initConfs.putAll(centerConfs);
        }

        if (CollectionUtils.isNonEmpty(initConfs)) {
            for (Map.Entry<String, String> entry : initConfs.entrySet()) {
                CACHE.put(entry.getKey(), new ConfDTO(entry.getKey(), entry.getValue()));
            }
        }
    }

    static ConfDTO get(String key) {
        return CACHE.get(key);
    }

    static void put(String key, String value) {
        if (StringUtils.isNotBlank(key)) {
            key = key.trim();
            CACHE.put(key, new ConfDTO(key, value));
        }
    }

    static void putAll(Map<String, String> confs) {
        for (Map.Entry<String, String> entry : confs.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    static Set<String> cachedKeys() {
        return CACHE.keySet();
    }

    static int cacheSize() {
        return CACHE.size();
    }
}
