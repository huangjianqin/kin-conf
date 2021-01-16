package org.kin.conf.diamond.dao;

import org.kin.conf.diamond.entity.ConfMsg;
import org.kin.conf.diamond.mapper.ConfMsgMapper;
import org.kin.framework.mybatis.BaseDao;

import java.util.Collection;
import java.util.List;

/**
 * @author huangjianqin
 * @date 2019/7/12
 */
public interface ConfMsgDao extends BaseDao<ConfMsg, ConfMsgMapper> {
    /**
     * 获取操作log消息, 根据changeTime排序(从小到大), 并且排序掉ids
     *
     * @param ids 需要排除(exclude)的操作log消息id
     */
    List<ConfMsg> get(Collection<Integer> ids);

    /**
     * 清除过期的操作log消息
     */
    int clean(long messageTimeout);
}
