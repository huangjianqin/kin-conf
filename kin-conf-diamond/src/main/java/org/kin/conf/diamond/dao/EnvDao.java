package org.kin.conf.diamond.dao;

import org.kin.conf.diamond.entity.Env;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author huangjianqin
 * @date 2019/7/12
 */
@Repository
public interface EnvDao extends JpaRepository<Env, String> {
}
