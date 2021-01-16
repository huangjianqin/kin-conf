package org.kin.conf.diamond.dao;

import org.kin.conf.diamond.entity.ConfLog;
import org.kin.conf.diamond.mapper.ConfLogMapper;
import org.kin.framework.mybatis.DaoSupport;
import org.springframework.stereotype.Component;

/**
 * @author huangjianqin
 * @date 2021/1/13
 */
@Component
public class ConfLogDaoImpl extends DaoSupport<ConfLog, ConfLogMapper> implements ConfLogDao {
}
