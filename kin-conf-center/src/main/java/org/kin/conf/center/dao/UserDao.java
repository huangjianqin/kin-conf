package org.kin.conf.center.dao;

import org.kin.conf.center.entity.User;

import java.util.List;

/**
 * @author huangjianqin
 * @date 2019/7/12
 */
public interface UserDao extends BaseDao<User> {
    List<User> list(int offset, int pageSize, String userName, int permission);
}
