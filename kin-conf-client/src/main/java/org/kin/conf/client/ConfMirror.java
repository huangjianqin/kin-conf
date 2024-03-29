package org.kin.conf.client;

import org.kin.conf.client.domain.ConfDTO;
import org.kin.framework.utils.CollectionUtils;
import org.kin.framework.utils.PropertiesUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author huangjianqin
 * @date 2019/7/8
 */
class ConfMirror {
    static ConfDTO get(String key) {
        Properties properties = PropertiesUtils.loadFileProperties(KinConf.getMirrorFile());
        if (properties != null && properties.containsKey(key)) {
            return new ConfDTO(key, properties.getProperty(key));
        }
        return null;
    }

    static Map<String, String> mirrorConfs() {
        Properties properties = PropertiesUtils.loadFileProperties(KinConf.getMirrorFile());
        Map<String, String> mirrorConfs = Collections.emptyMap();
        if (properties != null && CollectionUtils.isNonEmpty(properties.stringPropertyNames())) {
            mirrorConfs = new HashMap<>(properties.size());
            for (String name : properties.stringPropertyNames()) {
                mirrorConfs.put(name, properties.getProperty(name));
            }
        }

        return mirrorConfs;
    }

    static void writeMirror(Map<String, String> confs) {
        Properties properties = new Properties();
        properties.putAll(confs);
        PropertiesUtils.writeProperties(properties, KinConf.getMirrorFile());
    }
}
