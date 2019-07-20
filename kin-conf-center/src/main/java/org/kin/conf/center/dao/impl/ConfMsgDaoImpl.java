package org.kin.conf.center.dao.impl;

import org.kin.conf.center.dao.BaseDaoSupport;
import org.kin.conf.center.dao.ConfMsgDao;
import org.kin.conf.center.entity.ConfMsg;

import java.util.Collection;
import java.util.List;

/**
 * @author huangjianqin
 * @date 2019/7/13
 */
public class ConfMsgDaoImpl extends BaseDaoSupport<ConfMsg> implements ConfMsgDao {
    @Override
    public List<ConfMsg> get(Collection<Integer> excludes) {
        return getSession().createQuery("FROM ConfMsg Where id not in (:ids)", ConfMsg.class)
                .setParameter("ids", excludes)
                .list();
    }

    @Override
    public int clean(long messageTimeout) {
        return getSession()
                .createQuery("DELETE FROM ConfMsg WHERE changeTime <= :messageTimeout", Integer.class)
                .setParameter("messageTimeout", messageTimeout)
                .uniqueResult();
    }

}
