package org.kin.conf.diamond.domain;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalNotification;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import org.kin.framework.utils.ExceptionUtils;
import org.kin.framework.utils.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author huangjianqin
 * @date 2019/7/14
 * <p>
 * 应该说是异步刷盘, 有一定延迟, 但大大减少了磁盘IO
 */
@Service
public class Duplicatehelper implements DisposableBean {
    private static final Logger log = LoggerFactory.getLogger(Duplicatehelper.class);

    /** 副本根目录 */
    @Value("${kin.conf.duplicatePath}")
    private String duplicatePath;

    private Cache<String, Properties> cache = CacheBuilder.newBuilder()
            //最近一次访问10s后, 失效, flush到磁盘
            .expireAfterAccess(10, TimeUnit.SECONDS)
            .removalListener((RemovalNotification<String, Properties> notification) ->
                    PropertiesUtils.writeFileProperties(notification.getValue(), notification.getKey()))
            .build();

    public Duplicatehelper() {
    }

    public Duplicatehelper(String duplicatePath) {
        this.duplicatePath = duplicatePath;
    }

    private String parsePath(String appName, String env) {
        return duplicatePath.concat(File.separator).concat(appName).concat(File.separator)
                .concat(env + ".properties");
    }

    private Properties getProperties(String cacheUniqueKey) {
        try {
            return cache.get(cacheUniqueKey, () -> {
                Properties properties = PropertiesUtils.loadFileProperties(cacheUniqueKey);
                if (properties == null) {
                    properties = new Properties();
                }
                return properties;
            });
        } catch (ExecutionException e) {
            ExceptionUtils.log(e);
        }

        return null;
    }

    private Properties getProperties(String appName, String env) {
        String cacheUniqueKey = parsePath(appName, env);
        return getProperties(cacheUniqueKey);
    }

    public String get(String appName, String env, String key) {
        return getProperties(appName, env).getProperty(key);
    }

    public void set(String appName, String env, String key, String value) {
        getProperties(appName, env).setProperty(key, value);
    }

    public synchronized void clean(List<ConfUniqueKey> confUniqueKeys) {
        ListMultimap<String, String> cacheUniqueKey2Confs = MultimapBuilder.ListMultimapBuilder.hashKeys().arrayListValues().build();
        for (ConfUniqueKey confUniqueKey : confUniqueKeys) {
            String cacheUniqueKey = parsePath(confUniqueKey.getAppName(), confUniqueKey.getEnv());
            cacheUniqueKey2Confs.put(cacheUniqueKey, confUniqueKey.getKeyV());
        }

        for (Map.Entry<String, Collection<String>> entry : cacheUniqueKey2Confs.asMap().entrySet()) {
            Properties properties = getProperties(entry.getKey());

            if (Objects.nonNull(properties)) {
                List<String> removeKeys = new ArrayList<>();
                for (String key : properties.stringPropertyNames()) {
                    if (!entry.getValue().contains(key)) {
                        removeKeys.add(key);
                    }
                }

                for (String removeKey : removeKeys) {
                    properties.remove(removeKey);
                }
            }
        }

        flush();
    }

    public void flush() {
        cache.cleanUp();
    }

    @Override
    public void destroy() {
        flush();
    }
}
