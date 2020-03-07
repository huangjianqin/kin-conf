package org.kin.conf.diamond.service.impl;

import org.kin.conf.diamond.dao.*;
import org.kin.conf.diamond.domain.*;
import org.kin.conf.diamond.entity.*;
import org.kin.conf.diamond.service.AdminService;
import org.kin.framework.utils.CollectionUtils;
import org.kin.framework.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public ConfListResponse listConf(int offset, String appName, String env) {
        ConfListResponse resp = new ConfListResponse();
        if (offset >= 0 && StringUtils.isNotBlank(appName) && StringUtils.isNotBlank(env)) {
            long totalCount = confDao.count();

            Conf conf = new Conf();
            conf.setAppName(appName);
            conf.setEnv(env);

            Example<Conf> example = Example.of(conf);
            Pageable pageable = PageRequest.of(offset, Constants.DEFAULT_PAGE_SIZE);

            List<Conf> confs = confDao.findAll(example, pageable).getContent();

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
        confMsg.setKeyV(key);
        confMsg.setValue(value);
        confMsg.setChangeTime(System.currentTimeMillis());

        confMsgDao.save(confMsg);
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

        Optional<Conf> confOptional = confDao.findById(new ConfUniqueKey(appName, env, key));
        if (!confOptional.isPresent()) {
            return WebResponse.fail("配置不存在");
        }
        Conf conf = confOptional.get();

        confDao.delete(conf);
        //log
        ConfLog confLog = ConfLog.logDelete(conf.getEnv(), conf.getKeyV(), conf.getAppName(), conf.getDescription(), conf.getValue(), user.getAccount());
        confLogDao.save(confLog);

        sendConfMsg(conf.getAppName(), conf.getEnv(), conf.getKeyV(), null);

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

        Project project = projectDao.findById(conf.getAppName()).orElse(null);
        if (project == null) {
            return WebResponse.fail("应用不存在");
        }

        if (StringUtils.isBlank(conf.getEnv())) {
            return WebResponse.fail("环境不能为空");
        }

        Env env = envDao.findById(conf.getEnv()).orElse(null);
        if (env == null) {
            return WebResponse.fail("环境不存在");
        }

        //校验权限
        if (!user.hasPermission(conf.getAppName(), conf.getEnv())) {
            return WebResponse.fail("没有权限添加配置");
        }

        if (StringUtils.isBlank(conf.getKeyV())) {
            return WebResponse.fail("key不能为空");
        }
        conf.setKeyV(conf.getKeyV().trim());

        Conf dbConf = confDao.findById(new ConfUniqueKey(conf.getAppName(), conf.getEnv(), conf.getKeyV())).orElse(null);
        if (dbConf != null) {
            return WebResponse.fail("配置已存在, 不能重复添加");
        }

        if (conf.getValue() == null) {
            conf.setValue("");
        }

        confDao.save(conf);

        //log
        ConfLog confLog = ConfLog.logAdd(conf.getEnv(), conf.getKeyV(), conf.getAppName(), conf.getDescription(), conf.getValue(), user.getAccount());
        confLogDao.save(confLog);

        sendConfMsg(conf.getAppName(), conf.getEnv(), conf.getKeyV(), conf.getValue());

        return WebResponse.success();
    }

    @Override
    public WebResponse<String> update(User user, Conf conf) {
        if (StringUtils.isBlank(conf.getDescription())) {
            return WebResponse.fail("描述不能为空");
        }

        if (StringUtils.isBlank(conf.getKeyV())) {
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

        Conf dbConf = confDao.findById(new ConfUniqueKey(conf.getAppName(), conf.getEnv(), conf.getKeyV())).orElse(null);
        if (dbConf == null) {
            return WebResponse.fail("配置不存在");
        }

        if (conf.getValue() == null) {
            conf.setValue("");
        }

        dbConf.setDescription(conf.getDescription());
        dbConf.setValue(conf.getValue());
        confDao.save(dbConf);

        //log
        ConfLog confLog = ConfLog.logUpdate(conf.getEnv(), conf.getKeyV(), conf.getAppName(), conf.getDescription(), conf.getValue(), user.getAccount());
        confLogDao.save(confLog);

        sendConfMsg(dbConf.getAppName(), dbConf.getEnv(), dbConf.getKeyV(), dbConf.getValue());

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
