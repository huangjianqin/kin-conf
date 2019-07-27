package org.kin.conf.client.listener;

/**
 * @author huangjianqin
 * @date 2019/7/19
 */
@FunctionalInterface
public interface ConfChangeListener {
    /**
     * 添加listener或者配置变化时触发
     */
    void onChange(String key, String value);
}
