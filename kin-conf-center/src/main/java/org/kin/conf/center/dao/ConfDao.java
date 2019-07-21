package org.kin.conf.center.dao;

import org.kin.conf.center.domain.ConfUniqueKey;
import org.kin.conf.center.entity.Conf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author huangjianqin
 * @date 2019/7/12
 */
@Repository
public interface ConfDao extends JpaRepository<Conf, ConfUniqueKey> {
}
