package org.kin.conf.diamond.service.impl;

import org.kin.conf.diamond.dao.ConfDao;
import org.kin.conf.diamond.dao.ConfMsgDao;
import org.kin.conf.diamond.domain.*;
import org.kin.conf.diamond.entity.Conf;
import org.kin.conf.diamond.entity.ConfMsg;
import org.kin.framework.concurrent.Keeper;
import org.kin.framework.utils.CollectionUtils;
import org.kin.framework.utils.ExceptionUtils;
import org.kin.framework.utils.TimeUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    @Autowired
    private ConfDao confDao;
    @Autowired
    private ConfMsgDao confMsgDao;
    @Autowired
    private Duplicatehelper duplicatehelper;

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
                Sort sort = Sort.by("changeTime");
                confMsgs = confMsgDao.findAll(sort);
            }

            if (CollectionUtils.isNonEmpty(confMsgs)) {
                for (ConfMsg confMsg : confMsgs) {
                    readedMsgIds.add(confMsg.getId());

                    if (confMsg.getChangeTime() > maxChangeTime) {
                        maxChangeTime = confMsg.getChangeTime();
                    }

                    //同步到磁盘
                    duplicatehelper.set(confMsg.getAppName(), confMsg.getEnv(), confMsg.getKeyV(), confMsg.getValue());

                    List<DeferredResult<WebResponse<String>>> deferredResults = MonitorData.remove(confMsg.getAppName(), confMsg.getEnv(), confMsg.getKeyV());
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
                int offset = 0;
                int pageSize = Constants.DEFAULT_PAGE_SIZE;

                List<Conf> confs;
                List<ConfUniqueKey> confUniqueKeys = new ArrayList<>();
                while (CollectionUtils.isNonEmpty((confs = confDao.findAll(PageRequest.of(offset, pageSize)).getContent()))) {
                    for (Conf conf : confs) {
                        //同步到磁盘
                        duplicatehelper.set(conf.getAppName(), conf.getEnv(), conf.getKeyV(), conf.getValue());

                        confUniqueKeys.add(new ConfUniqueKey(conf.getAppName(), conf.getEnv(), conf.getKeyV()));
                    }
                    offset += pageSize;
                }

                //清掉无效配置
                duplicatehelper.clean(confUniqueKeys);
            } catch (Exception e) {
                ExceptionUtils.log(e);
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
