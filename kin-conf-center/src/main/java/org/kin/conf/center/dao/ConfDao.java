package org.kin.conf.center.dao;

import org.kin.conf.center.entity.Conf;

import java.util.List;

/**
 * @author huangjianqin
 * @date 2019/7/12
 */
public interface ConfDao extends BaseDao<Conf> {
    List<Conf> list(int offset, int pageSize, String env, String appName, String key);

    List<Conf> list(int offset, int pageSize);

    int count(String env, String appName, String key);
}
