package org.kin.conf.diamond.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.kin.conf.diamond.dao.ConfDao;
import org.kin.conf.diamond.dao.ConfMsgDao;
import org.kin.conf.diamond.domain.*;
import org.kin.conf.diamond.entity.Conf;
import org.kin.conf.diamond.entity.ConfMsg;
import org.kin.framework.concurrent.Keeper;
import org.kin.framework.utils.CollectionUtils;
import org.kin.framework.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author huangjianqin
 * @date 2019/7/12
 */
@Service
public class DiamondService implements InitializingBean, DisposableBean {
    private static final Logger log = LoggerFactory.getLogger(DiamondService.class);

    @Autowired
    private ConfDao confDao;
    @Autowired
    private ConfMsgDao confMsgDao;
    @Autowired
    private DuplicateHelper duplicatehelper;

    /** 副本同步间隔 */
    @Value("${kin.conf.duplicateInterval}")
    private int duplicateInterval;


    private List<Integer> readedMsgIds;
    private long maxChangeTime;

    private Keeper.KeeperStopper msgHandleKeeper;
    private Keeper.KeeperStopper duplicateKeeper;

    @Override
    public void destroy() {
        msgHandleKeeper.stop();
        duplicateKeeper.stop();
    }

    @Override
    public void afterPropertiesSet() {
        readedMsgIds = Collections.synchronizedList(new ArrayList<>());
        msgHandleKeeper = Keeper.keep(() -> {
            List<ConfMsg> confMsgs;
            if (CollectionUtils.isNonEmpty(readedMsgIds)) {
                confMsgs = confMsgDao.get(readedMsgIds);
            } else {
                LambdaQueryWrapper<ConfMsg> query = Wrappers.lambdaQuery(ConfMsg.class).orderByAsc(ConfMsg::getChangeTime);
                confMsgs = confMsgDao.selectList(query);
            }

            if (CollectionUtils.isNonEmpty(confMsgs)) {
                for (ConfMsg confMsg : confMsgs) {
                    readedMsgIds.add(confMsg.getId());

                    if (confMsg.getChangeTime() > maxChangeTime) {
                        maxChangeTime = confMsg.getChangeTime();
                    }

                    //同步到磁盘
                    duplicatehelper.set(confMsg.getAppName(), confMsg.getEnv(), confMsg.getKey(), confMsg.getValue());

                    List<DeferredResult<WebResponse<String>>> deferredResults = MonitorData.remove(confMsg.getAppName(), confMsg.getEnv(), confMsg.getKey());
                    if (CollectionUtils.isNonEmpty(deferredResults)) {
                        for (DeferredResult<WebResponse<String>> deferredResult : deferredResults) {
                            deferredResult.setResult(WebResponse.success("monitor key update"));
                        }
                    }
                }
            }

            if (TimeUtils.timestamp() % duplicateInterval == 0) {
                duplicatehelper.flush();
                confMsgDao.clean(maxChangeTime);
                readedMsgIds.clear();
                maxChangeTime = 0;
            }

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {

            }
        });

        duplicateKeeper = Keeper.keep(() -> {
            try {
                long sleepTime = duplicateInterval - TimeUtils.timestamp() % duplicateInterval;
                if (sleepTime > 0 && sleepTime < duplicateInterval) {
                    TimeUnit.SECONDS.sleep(sleepTime);
                }
            } catch (InterruptedException e) {

            }

            try {
                //页数
                int pageId = 0;
                int pageSize = Constants.DEFAULT_PAGE_SIZE;

                List<Conf> confs;
                List<ConfUniqueKey> confUniqueKeys = new ArrayList<>();
                Page<Conf> page = new Page<>(pageId, pageSize);
                while (CollectionUtils.isNonEmpty((confs = confDao.selectPage(page, null).getRecords()))) {
                    for (Conf conf : confs) {
                        //同步到磁盘
                        duplicatehelper.set(conf.getAppName(), conf.getEnv(), conf.getKey(), conf.getValue());

                        confUniqueKeys.add(new ConfUniqueKey(conf.getAppName(), conf.getEnv(), conf.getKey()));
                    }
                    pageId += 1;
                    page = new Page<>(pageId, pageSize);
                }

                //清掉无效配置
                duplicatehelper.clean(confUniqueKeys);
            } catch (Exception e) {
                log.error("", e);
            }

            try {
                TimeUnit.SECONDS.sleep(duplicateInterval);
            } catch (InterruptedException e) {

            }
        });
    }

    public String get(String appName, String env, String key) {
        return duplicatehelper.get(appName, env, key);
    }
}
