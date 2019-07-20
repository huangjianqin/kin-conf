package org.kin.conf.center.service;

import org.kin.conf.center.domain.CommonResponse;
import org.kin.conf.center.domain.ConfListResponse;
import org.kin.conf.center.entity.Conf;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.Map;

/**
 * @author huangjianqin
 * @date 2019/7/12
 */
public interface AdminService {
    ConfListResponse listConf(int offset, String env, String appName, String key);

    CommonResponse<String> delete(String appName, String env, String key);

    CommonResponse<String> add(Conf conf);

    CommonResponse<String> update(Conf conf);

    CommonResponse<Map<String, String>> getConfs(String appName, String env, List<String> keys);

    DeferredResult<CommonResponse<String>> monitor(String appName, String env, List<String> keys);
}
