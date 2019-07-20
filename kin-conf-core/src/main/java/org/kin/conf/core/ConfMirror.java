package org.kin.conf.core;

import org.kin.conf.core.domain.ConfDTO;
import org.kin.framework.utils.CollectionUtils;
import org.kin.framework.utils.PropertiesUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author huangjianqin
 * @date 2019/7/8
 */
class ConfMirror {
    static ConfDTO get(String key) {
        Properties properties = PropertiesUtils.loadClassPathProperties(KinConf.getMirrorFile());
        if (properties != null && properties.containsKey(key)) {
            return new ConfDTO(key, properties.getProperty(key));
        }
        return null;
    }

    static Map<String, String> mirrorConfs() {
        Map<String, String> mirrorConfs = new HashMap<>();
        Properties properties = PropertiesUtils.loadClassPathProperties(KinConf.getMirrorFile());
        if (properties != null && CollectionUtils.isNonEmpty(properties.stringPropertyNames())) {
            for (String name : properties.stringPropertyNames()) {
                mirrorConfs.put(name, properties.getProperty(name));
            }
        }

        return mirrorConfs;
    }

    static void writeMirror(Map<String, String> confs) {
        Properties properties = new Properties();
        properties.putAll(confs);
        PropertiesUtils.writeFileProperties(properties, KinConf.getMirrorFile());
    }
}
