package org.kin.conf.center.dao;

import org.kin.conf.center.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author huangjianqin
 * @date 2019/7/12
 */
@Repository
public interface UserDao extends JpaRepository<User, String> {
}
