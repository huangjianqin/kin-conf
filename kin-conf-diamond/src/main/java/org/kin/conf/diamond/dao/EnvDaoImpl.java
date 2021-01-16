package org.kin.conf.diamond.dao;

import org.kin.conf.diamond.entity.Env;
import org.kin.conf.diamond.mapper.EnvMapper;
import org.kin.framework.mybatis.DaoSupport;
import org.springframework.stereotype.Component;

/**
 * @author huangjianqin
 * @date 2021/1/13
 */
@Component
public class EnvDaoImpl extends DaoSupport<Env, EnvMapper> implements EnvDao {
}
