package org.kin.conf.diamond.converter;

import org.kin.framework.utils.CollectionUtils;
import org.kin.framework.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huangjianqin
 * @date 2019/7/22
 */
public class JpaPermissionStrConverter implements AttributeConverter<Map<String, List<String>>, String> {
    private static final Logger log = LoggerFactory.getLogger(JpaPermissionStrConverter.class);

    @Override
    public String convertToDatabaseColumn(Map<String, List<String>> stringListMap) {
        if (CollectionUtils.isEmpty(stringListMap)) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : stringListMap.entrySet()) {
            if (CollectionUtils.isNonEmpty(entry.getValue())) {
                sb.append(entry.getKey());
                for (String env : entry.getValue()) {
                    sb.append("#").append(env);
                }
                sb.append(",");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    @Override
    public Map<String, List<String>> convertToEntityAttribute(String s) {
        Map<String, List<String>> result = new HashMap<>();
        if (StringUtils.isNotBlank(s)) {
            try {
                for (String split1 : s.split(",")) {
                    try {
                        String[] splits = split1.split("#");
                        String appName = splits[0];
                        List<String> envs = Arrays.asList(Arrays.copyOfRange(splits, 1, splits.length));
                        result.put(appName, envs);
                    } catch (Exception e) {
                        log.error("", e);
                    }
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }

        return result;
    }
}
