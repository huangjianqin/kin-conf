package org.kin.conf.client;

import org.kin.conf.client.listener.ConfChangeListener;
import org.kin.framework.utils.CollectionUtils;
import org.kin.framework.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huangjianqin
 * @date 2019/7/19
 */
public class ConfListenerManager {
    private static final Logger log = LoggerFactory.getLogger(ConfListenerManager.class);
    private static final Map<String, List<ConfChangeListener>> LISTENERS = new ConcurrentHashMap<>();

    private ConfListenerManager() {
    }

    public static boolean addListener(String key, ConfChangeListener listener) {
        if (listener == null || StringUtils.isBlank(key)) {
            return false;
        }
        key = key.trim();
        try {
            String value = KinConf.get(key);
            listener.onChange(key, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        synchronized (ConfListenerManager.class) {
            List<ConfChangeListener> listeners = LISTENERS.get(key);
            if (listeners == null) {
                listeners = new ArrayList<>();
            } else {
                listeners = new ArrayList<>(listeners);
            }
            listeners.add(listener);
            LISTENERS.put(key, listeners);
        }

        return true;
    }

    public static void onConfChange(String key, String value) {
        if (StringUtils.isBlank(key)) {
            return;
        }

        key = key.trim();
        List<ConfChangeListener> listeners = LISTENERS.get(key);
        if (CollectionUtils.isNonEmpty(listeners)) {
            for (ConfChangeListener listener : listeners) {
                try {
                    listener.onChange(key, value);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }

    }
}
