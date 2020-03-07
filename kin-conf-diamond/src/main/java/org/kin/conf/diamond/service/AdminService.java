package org.kin.conf.diamond.service;

import org.kin.conf.diamond.domain.ConfListResponse;
import org.kin.conf.diamond.domain.WebResponse;
import org.kin.conf.diamond.entity.Conf;
import org.kin.conf.diamond.entity.User;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.Map;

/**
 * @author huangjianqin
 * @date 2019/7/12
 */
public interface AdminService {
    /**
     * 获取配置
     *
     * @param offset  offset
     * @param appName appName
     * @param env     环境
     * @return 配置列表
     */
    ConfListResponse listConf(int offset, String appName, String env);

    /**
     * 删除配置
     * @param user 用户名
     * @param appName appName
     * @param env 环境
     * @param key 配置key
     * @return 删除配置结果
     */
    WebResponse<String> delete(User user, String appName, String env, String key);

    /**
     * 增加配置
     * @param user 用户名
     * @param conf 配置信息
     * @return 增加配置结果
     */
    WebResponse<String> add(User user, Conf conf);

    /**
     * 更新配置
     * @param user 用户名
     * @param conf 配置信息
     * @return 更新配置结果
     */
    WebResponse<String> update(User user, Conf conf);

    /**
     * 获取配置
     * @param appName appName
     * @param env 环境
     * @param keys 配置keys
     * @return 配置keys对应的配置s
     */
    WebResponse<Map<String, String>> getConfs(String appName, String env, List<String> keys);

    /**
     * 监听配置变化
     * @param appName appName
     * @param env 环境
     * @param keys 配置keys
     * @return 配置keys对应的配置信息变化s
     */
    DeferredResult<WebResponse<String>> monitor(String appName, String env, List<String> keys);
}
