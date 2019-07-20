package org.kin.conf.center.service.impl;

import org.kin.conf.center.dao.*;
import org.kin.conf.center.domain.*;
import org.kin.conf.center.entity.*;
import org.kin.conf.center.service.AdminService;
import org.kin.framework.utils.CollectionUtils;
import org.kin.framework.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private CenterServiceImpl centerServiceImpl;

    @Override
    public ConfListResponse listConf(int offset, String env, String appName, String key) {
        ConfListResponse resp = new ConfListResponse();
        if (offset >= 0 && StringUtils.isNotBlank(appName) && StringUtils.isNotBlank(key) && StringUtils.isNotBlank(env)) {
            int totalCount = confDao.count(env, appName, key);
            List<Conf> confs = confDao.list(offset, Constants.DEFAULT_PAGE_SIZE, env, appName, key);

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
    public CommonResponse<String> delete(String appName, String env, String key) {
        if (StringUtils.isBlank(appName)) {
            return CommonResponse.fail("应用不能为空");
        }

        if (StringUtils.isBlank(key)) {
            return CommonResponse.fail("配置key不能为空");
        }

        if (StringUtils.isBlank(env)) {
            return CommonResponse.fail("配置环境不能为空");
        }

        Conf conf = confDao.get(new ConfUniqueKey(appName, env, key));
        if (conf == null) {
            return CommonResponse.fail("配置不存在");
        }
        //TODO 校验权限

        if (confDao.delete(conf)) {
            return CommonResponse.fail("删除配置异常");
        }

        //log
        //TODO operator
        ConfLog confLog = ConfLog.logDelete(conf.getEnv(), conf.getKey(), conf.getAppName(), conf.getDesc(), conf.getValue(), "");
        confLogDao.insert(confLog);

        sendConfMsg(conf.getAppName(), conf.getEnv(), conf.getKey(), null);

        return CommonResponse.success();
    }

    @Override
    public CommonResponse<String> add(Conf conf) {
        if (StringUtils.isBlank(conf.getDesc())) {
            return CommonResponse.fail("描述不能为空");
        }

        if (StringUtils.isBlank(conf.getAppName())) {
            return CommonResponse.fail("应用名不能为空");
        }

        //TODO 校验权限

        Project project = projectDao.get(conf.getAppName());
        if (project == null) {
            return CommonResponse.fail("应用不存在");
        }

        if (StringUtils.isBlank(conf.getEnv())) {
            return CommonResponse.fail("环境不能为空");
        }

        Env env = envDao.get(conf.getEnv());
        if (env == null) {
            return CommonResponse.fail("环境不存在");
        }

        if (StringUtils.isBlank(conf.getKey())) {
            return CommonResponse.fail("key不能为空");
        }
        conf.setKey(conf.getKey().trim());

        Conf dbConf = confDao.get(new ConfUniqueKey(conf.getAppName(), conf.getEnv(), conf.getKey()));
        if (dbConf != null) {
            return CommonResponse.fail("配置已存在, 不能重复添加");
        }

        if (conf.getValue() == null) {
            conf.setValue("");
        }

        if (confDao.insert(conf)) {
            return CommonResponse.fail("添加配置异常");
        }

        //log
        //TODO operator
        ConfLog confLog = ConfLog.logAdd(conf.getEnv(), conf.getKey(), conf.getAppName(), conf.getDesc(), conf.getValue(), "");
        confLogDao.insert(confLog);

        sendConfMsg(conf.getAppName(), conf.getEnv(), conf.getKey(), conf.getValue());

        return CommonResponse.success();
    }

    @Override
    public CommonResponse<String> update(Conf conf) {
        if (StringUtils.isBlank(conf.getDesc())) {
            return CommonResponse.fail("描述不能为空");
        }

        if (StringUtils.isBlank(conf.getKey())) {
            return CommonResponse.fail("key不能为空");
        }

        Conf dbConf = confDao.get(new ConfUniqueKey(conf.getAppName(), conf.getEnv(), conf.getKey()));
        if (dbConf == null) {
            return CommonResponse.fail("配置不存在");
        }

        if (conf.getValue() == null) {
            conf.setValue("");
        }

        dbConf.setDesc(conf.getDesc());
        dbConf.setValue(conf.getValue());
        if (confDao.update(dbConf)) {
            return CommonResponse.fail("更新配置异常");
        }

        //log
        //TODO operator
        ConfLog confLog = ConfLog.logUpdate(conf.getEnv(), conf.getKey(), conf.getAppName(), conf.getDesc(), conf.getValue(), "");
        confLogDao.insert(confLog);

        sendConfMsg(dbConf.getAppName(), dbConf.getEnv(), dbConf.getKey(), dbConf.getValue());

        return CommonResponse.success();
    }

    //-----------------------------------------------------------------rest api-----------------------------------------
    @Override
    public CommonResponse<Map<String, String>> getConfs(String appName, String env, List<String> keys) {
        if (StringUtils.isBlank(appName)) {
            return CommonResponse.fail("应用不能为空");
        }
        appName = appName.trim();

        if (StringUtils.isBlank(env)) {
            return CommonResponse.fail("环境不能为空");
        }
        env = env.trim();

        if (CollectionUtils.isEmpty(keys)) {
            return CommonResponse.fail("请求key列表不能为空");
        }

        Map<String, String> result = new HashMap<>();
        for (String key : keys) {
            if (key != null) {
                key = key.trim();
            }

            String value = "";
            if (key != null && 4 <= key.length() || key.length() <= 100) {
                value = centerServiceImpl.get(appName, env, key);
            }

            result.put(key, value);
        }

        return CommonResponse.success(result);
    }

    @Override
    public DeferredResult<CommonResponse<String>> monitor(String appName, String env, List<String> keys) {
        DeferredResult<CommonResponse<String>> deferredResult = new DeferredResult(10000L,
                CommonResponse.<String>success("monitor timeout, no key value update"));

        if (StringUtils.isBlank(appName)) {
            deferredResult.setResult(CommonResponse.fail("应用不能为空"));
            return deferredResult;
        }
        appName = appName.trim();

        if (StringUtils.isBlank(env)) {
            deferredResult.setResult(CommonResponse.fail("环境不能为空"));
            return deferredResult;
        }
        env = env.trim();

        if (CollectionUtils.isEmpty(keys)) {
            deferredResult.setResult(CommonResponse.fail("请求key列表不能为空"));
            return deferredResult;
        }

        Map<String, String> result = new HashMap<>();
        for (String key : keys) {
            if (key != null) {
                key = key.trim();
            }

            if (key != null && 4 <= key.length() || key.length() <= 100) {
                MonitorData.add(appName, env, key, deferredResult);
            }
        }

        return deferredResult;
    }
}
