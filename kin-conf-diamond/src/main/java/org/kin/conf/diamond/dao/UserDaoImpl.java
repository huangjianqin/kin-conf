package org.kin.conf.diamond.dao;

import org.kin.conf.diamond.entity.User;
import org.kin.conf.diamond.mapper.UserMapper;
import org.kin.framework.mybatis.DaoSupport;
import org.springframework.stereotype.Component;

/**
 * @author huangjianqin
 * @date 2021/1/13
 */
@Component
public class UserDaoImpl extends DaoSupport<User, UserMapper> implements UserDao {
}
