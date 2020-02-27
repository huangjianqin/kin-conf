package org.kin.conf.client.listener.impl;

import org.kin.conf.client.domain.BeanConfWrapper;
import org.kin.conf.client.listener.ConfChangeListener;
import org.kin.conf.client.utils.BeanConfRefreshHelper;
import org.kin.framework.utils.CollectionUtils;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huangjianqin
 * @date 2019/7/19
 */
public class BeanConfRefresher implements ConfChangeListener {
    private ApplicationContext context;
    private Map<String, List<BeanConfWrapper>> beanConfs = new HashMap<>();

    public BeanConfRefresher(ApplicationContext context) {
        this.context = context;
    }

    public void watch(String key, BeanConfWrapper wrapper) {
        key = key.trim();
        synchronized (this) {
            List<BeanConfWrapper> wrappers = beanConfs.get(key);
            if (wrappers == null) {
                wrappers = new ArrayList<>();
            } else {
                wrappers = new ArrayList<>(wrappers);
            }

            //避免重复
            for (BeanConfWrapper item : wrappers) {
                if (item.equals(wrapper)) {
                    return;
                }
            }

            wrappers.add(wrapper);
            beanConfs.put(key, wrappers);
        }
    }

    @Override
    public void onChange(String key, String value) {
        List<BeanConfWrapper> wrappers = beanConfs.get(key);
        if (CollectionUtils.isNonEmpty(wrappers)) {
            for (BeanConfWrapper wrapper : wrappers) {
                BeanConfRefreshHelper.refreshBeanField(context, wrapper, value, null);
            }
        }
    }
}
