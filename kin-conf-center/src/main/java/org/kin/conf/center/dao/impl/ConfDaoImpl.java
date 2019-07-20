package org.kin.conf.center.dao.impl;

import org.kin.conf.center.dao.BaseDaoSupport;
import org.kin.conf.center.dao.ConfDao;
import org.kin.conf.center.entity.Conf;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author huangjianqin
 * @date 2019/7/12
 */
@Repository
public class ConfDaoImpl extends BaseDaoSupport<Conf> implements ConfDao {

    @Override
    public List<Conf> list(int offset, int pageSize, String env, String appName, String key) {
        return getSession()
                .createQuery("FROM Conf WHERE env = :env and appName = :appName and key LIKE :key", Conf.class)
                .setParameter("env", env)
                .setParameter("appName", appName)
                .setParameter("key", "%" + key + "%")
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .list();
    }

    @Override
    public List<Conf> list(int offset, int pageSize) {
        return getSession()
                .createQuery("FROM Conf", Conf.class)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .list();
    }

    @Override
    public int count(String env, String appName, String key) {
        return getSession()
                .createQuery("SELECT COUNT(*) FROM Conf WHERE env = :env and appName = :appName and key LIKE :key", Integer.class)
                .setParameter("env", env)
                .setParameter("appName", appName)
                .setParameter("key", "%" + key + "%")
                .uniqueResult();
    }
}
