package org.kin.conf.diamond.dao;

import org.kin.conf.diamond.entity.User;
import org.kin.conf.diamond.mapper.UserMapper;
import org.kin.framework.mybatis.BaseDao;

/**
 * @author huangjianqin
 * @date 2019/7/12
 */
public interface UserDao extends BaseDao<User, UserMapper> {
}
