package org.kin.conf.diamond.converter;

import org.kin.framework.mybatis.SimpleTypeHandler;
import org.kin.framework.utils.CollectionUtils;
import org.kin.framework.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理{@link org.kin.conf.diamond.entity.User} 成员域`permissionData`类型转换
 *
 * @author huangjianqin
 * @date 2021/1/14
 */
@Component
public class PermissionDataTypeHandler extends SimpleTypeHandler<String, Map<String, List<String>>> {
    private static final Logger log = LoggerFactory.getLogger(PermissionDataTypeHandler.class);

    @Override
    protected String convertToDbValue(Map<String, List<String>> obj) {
        if (CollectionUtils.isEmpty(obj)) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : obj.entrySet()) {
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
    protected Map<String, List<String>> convertToJavaValue(String columnValue) {
        Map<String, List<String>> result = new HashMap<>();
        if (StringUtils.isNotBlank(columnValue)) {
            try {
                for (String split1 : columnValue.split(",")) {
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
