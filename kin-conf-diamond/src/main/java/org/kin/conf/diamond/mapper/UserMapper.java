package org.kin.conf.diamond.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.kin.conf.diamond.entity.User;

/**
 * @author huangjianqin
 * @date 2021/1/13
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
