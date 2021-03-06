package org.kin.conf.diamond.dao;

import org.kin.conf.diamond.entity.Project;
import org.kin.conf.diamond.mapper.ProjectMapper;
import org.kin.framework.mybatis.DaoSupport;
import org.springframework.stereotype.Component;

/**
 * @author huangjianqin
 * @date 2021/1/13
 */
@Component
public class ProjectDaoImpl extends DaoSupport<Project, ProjectMapper> implements ProjectDao {
}
