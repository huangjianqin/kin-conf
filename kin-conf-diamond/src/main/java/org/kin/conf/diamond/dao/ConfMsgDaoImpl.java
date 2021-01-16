package org.kin.conf.diamond.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.kin.conf.diamond.entity.ConfMsg;
import org.kin.conf.diamond.mapper.ConfMsgMapper;
import org.kin.framework.mybatis.DaoSupport;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * @author huangjianqin
 * @date 2021/1/13
 */
@Component
public class ConfMsgDaoImpl extends DaoSupport<ConfMsg, ConfMsgMapper> implements ConfMsgDao {
    @Override
    public List<ConfMsg> get(Collection<Integer> ids) {
        LambdaQueryWrapper<ConfMsg> query = Wrappers.lambdaQuery(ConfMsg.class).notIn(ConfMsg::getId, ids).orderByAsc(ConfMsg::getChangeTime);
        return mapper.selectList(query);
    }

    @Override
    public int clean(long messageTimeout) {
        LambdaQueryWrapper<ConfMsg> query = Wrappers.lambdaQuery(ConfMsg.class).le(ConfMsg::getChangeTime, messageTimeout);
        return mapper.delete(query);
    }
}
