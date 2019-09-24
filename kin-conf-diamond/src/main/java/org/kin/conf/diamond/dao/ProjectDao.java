package org.kin.conf.diamond.dao;

import org.kin.conf.diamond.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author huangjianqin
 * @date 2019/7/12
 */
@Repository
public interface ProjectDao extends JpaRepository<Project, String> {
}