package org.kin.conf.center.dao;

import org.kin.conf.center.entity.ConfMsg;

import java.util.Collection;
import java.util.List;

/**
 * @author huangjianqin
 * @date 2019/7/12
 */
public interface ConfMsgDao extends BaseDao<ConfMsg> {
    List<ConfMsg> get(Collection<Integer> excludes);

    int clean(long messageTimeout);
}
