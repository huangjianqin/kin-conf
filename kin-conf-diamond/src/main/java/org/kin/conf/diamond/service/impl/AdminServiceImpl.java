package org.kin.conf.diamond.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.kin.conf.diamond.dao.*;
import org.kin.conf.diamond.domain.*;
import org.kin.conf.diamond.entity.*;
import org.kin.conf.diamond.service.AdminService;
import org.kin.framework.utils.CollectionUtils;
import org.kin.framework.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author huangjianqin
 * @date 2019/7/14
 */
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private ConfDao confDao;
    @Autowired
    private ConfMsgDao confMsgDao;
    @Autowired
    private ConfLogDao confLogDao;
    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private EnvDao envDao;
    @Autowired
    private DiamondService diamondService;

    @Override
    public ConfListResponse listConf(int page, String appName, String env) {
        ConfListResponse resp = new ConfListResponse();
        if (page > 0 && StringUtils.isNotBlank(appName) && StringUtils.isNotBlank(env)) {
            long totalCount = confDao.selectCount(null);

            LambdaQueryWrapper<Conf> query =
                    Wrappers.lambdaQuery(Conf.class)
                            .eq(Conf::getAppName, appName)
                            .eq(Conf::getEnv, env);
            List<Conf> confs = confDao.selectPage(new Page<>(page, Constants.DEFAULT_PAGE_SIZE), query).getRecords();

            resp.setTotalCount(totalCount);
            if (CollectionUtils.isNonEmpty(confs)) {
                resp.setData(confs);
            }
        }

        return resp;
    }

    private void sendConfMsg(String appName, String env, String key, String value) {
        ConfMsg confMsg = new ConfMsg();
        confMsg.setAppName(appName);
        confMsg.setEnv(env);
        confMsg.setKey(key);
        confMsg.setValue(value);
        confMsg.setChangeTime(System.currentTimeMillis());

        confMsgDao.insert(confMsg);
    }

    @Override
    public WebResponse<String> delete(User user, String appName, String env, String key) {
        if (StringUtils.isBlank(appName)) {
            return WebResponse.fail("应用不能为空");
        }

        if (StringUtils.isBlank(key)) {
            return WebResponse.fail("配置key不能为空");
        }

        if (StringUtils.isBlank(env)) {
            return WebResponse.fail("配置环境不能为空");
        }

        //校验权限
        if (!user.hasPermission(appName, env)) {
            return WebResponse.fail("没有权限删除该配置");
        }

        ConfUniqueKey pk = new ConfUniqueKey(appName, env, key);
        Conf conf = confDao.selectById(pk);
        if (Objects.isNull(conf)) {
            return WebResponse.fail("配置不存在");
        }

        confDao.deleteById(pk);
        //log
        ConfLog confLog = ConfLog.logDelete(conf.getEnv(), conf.getKey(), conf.getAppName(), conf.getDescription(), conf.getValue(), user.getAccount());
        confLogDao.insert(confLog);

        sendConfMsg(conf.getAppName(), conf.getEnv(), conf.getKey(), null);

        return WebResponse.success();
    }

    @Override
    public WebResponse<String> add(User user, Conf conf) {
        if (StringUtils.isBlank(conf.getDescription())) {
            return WebResponse.fail("描述不能为空");
        }

        if (StringUtils.isBlank(conf.getAppName())) {
            return WebResponse.fail("应用名不能为空");
        }

        Project project = projectDao.selectById(conf.getAppName());
        if (project == null) {
            return WebResponse.fail("应用不存在");
        }

        if (StringUtils.isBlank(conf.getEnv())) {
            return WebResponse.fail("环境不能为空");
        }

        Env env = envDao.selectById(conf.getEnv());
        if (env == null) {
            return WebResponse.fail("环境不存在");
        }

        //校验权限
        if (!user.hasPermission(conf.getAppName(), conf.getEnv())) {
            return WebResponse.fail("没有权限添加配置");
        }

        if (StringUtils.isBlank(conf.getKey())) {
            return WebResponse.fail("key不能为空");
        }
        conf.setKey(conf.getKey().trim());

        Conf dbConf = confDao.selectById(new ConfUniqueKey(conf.getAppName(), conf.getEnv(), conf.getKey()));
        if (dbConf != null) {
            return WebResponse.fail("配置已存在, 不能重复添加");
        }

        if (conf.getValue() == null) {
            conf.setValue("");
        }

        confDao.insert(conf);

        //log
        ConfLog confLog = ConfLog.logAdd(conf.getEnv(), conf.getKey(), conf.getAppName(), conf.getDescription(), conf.getValue(), user.getAccount());
        confLogDao.insert(confLog);

        sendConfMsg(conf.getAppName(), conf.getEnv(), conf.getKey(), conf.getValue());

        return WebResponse.success();
    }

    @Override
    public WebResponse<String> update(User user, Conf conf) {
        if (StringUtils.isBlank(conf.getDescription())) {
            return WebResponse.fail("描述不能为空");
        }

        if (StringUtils.isBlank(conf.getKey())) {
            return WebResponse.fail("key不能为空");
        }

        if (StringUtils.isBlank(conf.getAppName())) {
            return WebResponse.fail("应用不能为空");
        }

        if (StringUtils.isBlank(conf.getEnv())) {
            return WebResponse.fail("配置环境不能为空");
        }

        //校验权限
        if (!user.hasPermission(conf.getAppName(), conf.getEnv())) {
            return WebResponse.fail("没有权限删除该配置");
        }

        Conf dbConf = confDao.selectById(new ConfUniqueKey(conf.getAppName(), conf.getEnv(), conf.getKey()));
        if (dbConf == null) {
            return WebResponse.fail("配置不存在");
        }

        if (conf.getValue() == null) {
            conf.setValue("");
        }

        dbConf.setDescription(conf.getDescription());
        dbConf.setValue(conf.getValue());
        confDao.updateById(dbConf);

        //log
        ConfLog confLog = ConfLog.logUpdate(conf.getEnv(), conf.getKey(), conf.getAppName(), conf.getDescription(), conf.getValue(), user.getAccount());
        confLogDao.insert(confLog);

        sendConfMsg(dbConf.getAppName(), dbConf.getEnv(), dbConf.getKey(), dbConf.getValue());

        return WebResponse.success();
    }

    //-----------------------------------------------------------------rest api-----------------------------------------

    @Override
    public WebResponse<Map<String, String>> getConfs(String appName, String env, List<String> keys) {
        if (StringUtils.isBlank(appName)) {
            return WebResponse.fail("应用不能为空");
        }
        appName = appName.trim();

        if (StringUtils.isBlank(env)) {
            return WebResponse.fail("环境不能为空");
        }
        env = env.trim();

        if (CollectionUtils.isEmpty(keys)) {
            return WebResponse.fail("请求key列表不能为空");
        }

        Map<String, String> result = new HashMap<>(keys.size());
        for (String key : keys) {
            if (key != null) {
                key = key.trim();
            }

            String value = "";
            if (key != null && (4 <= key.length() && key.length() <= 100)) {
                value = diamondService.get(appName, env, key);
            }

            result.put(key, value);
        }

        return WebResponse.success(result);
    }

    @Override
    public DeferredResult<WebResponse<String>> monitor(String appName, String env, List<String> keys) {
        DeferredResult<WebResponse<String>> deferredResult = new DeferredResult(10000L,
                WebResponse.<String>success("monitor timeout, no key value update"));

        if (StringUtils.isBlank(appName)) {
            deferredResult.setResult(WebResponse.fail("应用不能为空"));
            return deferredResult;
        }
        appName = appName.trim();

        if (StringUtils.isBlank(env)) {
            deferredResult.setResult(WebResponse.fail("环境不能为空"));
            return deferredResult;
        }
        env = env.trim();

        if (CollectionUtils.isEmpty(keys)) {
            deferredResult.setResult(WebResponse.fail("请求key列表不能为空"));
            return deferredResult;
        }
        
        for (String key : keys) {
            if (key != null) {
                key = key.trim();
            }

            if (key != null && (4 <= key.length() && key.length() <= 100)) {
                MonitorData.add(appName, env, key, deferredResult);
            }
        }
        return deferredResult;
    }
}
