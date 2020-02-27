package org.kin.conf.client;

import org.kin.conf.client.domain.ConfDTO;
import org.kin.framework.utils.CollectionUtils;
import org.kin.framework.utils.StringUtils;

import java.util.*;
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

        //从diamond更新配置
        Map<String, String> diamondConfs = Collections.emptyMap();
        if (CollectionUtils.isNonEmpty(mirrorConfs.keySet())) {
            diamondConfs = ConfDiamond.get(mirrorConfs.keySet());
        }

        if (CollectionUtils.isNonEmpty(mirrorConfs)) {
            initConfs.putAll(mirrorConfs);
        }
        if (CollectionUtils.isNonEmpty(diamondConfs)) {
            initConfs.putAll(diamondConfs);
        }

        if (CollectionUtils.isNonEmpty(initConfs)) {
            Map<String, String> newMirrorConfs = new HashMap<>(initConfs.size());
            for (Map.Entry<String, String> entry : initConfs.entrySet()) {
                if (Objects.nonNull(entry.getValue())) {
                    CACHE.put(entry.getKey(), new ConfDTO(entry.getKey(), entry.getValue()));
                    newMirrorConfs.put(entry.getKey(), entry.getValue());
                }
            }
            //马上刷新镜像
            ConfMirror.writeMirror(newMirrorConfs);
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
