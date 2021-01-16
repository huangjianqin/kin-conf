package org.kin.conf.diamond.dao;

import org.kin.conf.diamond.entity.Conf;
import org.kin.conf.diamond.mapper.ConfMapper;
import org.kin.framework.mybatis.DaoSupport;
import org.springframework.stereotype.Component;

/**
 * @author huangjianqin
 * @date 2021/1/13
 */
@Component
public class ConfDaoImpl extends DaoSupport<Conf, ConfMapper> implements ConfDao {
}
