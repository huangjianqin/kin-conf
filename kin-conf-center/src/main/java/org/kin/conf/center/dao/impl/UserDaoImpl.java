package org.kin.conf.center.dao.impl;

import org.kin.conf.center.dao.BaseDaoSupport;
import org.kin.conf.center.dao.UserDao;
import org.kin.conf.center.entity.User;

import java.util.List;

/**
 * @author huangjianqin
 * @date 2019/7/13
 */
public class UserDaoImpl extends BaseDaoSupport<User> implements UserDao {
    @Override
    public List<User> list(int offset, int pageSize, String name, int permission) {
        return getSession().createQuery("FROM User WHERE name like :name and permission = :permission", User.class)
                .setParameter("name", name)
                .setParameter("permission", permission)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .list();
    }
}
