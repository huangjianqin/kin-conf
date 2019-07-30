package org.kin.conf.diamond.dao;

import org.kin.conf.diamond.entity.ConfMsg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

/**
 * @author huangjianqin
 * @date 2019/7/12
 */
@Repository
public interface ConfMsgDao extends JpaRepository<ConfMsg, Integer> {
    @Query("FROM ConfMsg Where id not in (:ids) ORDER BY changeTime")
    List<ConfMsg> get(@Param("ids") Collection<Integer> ids);

    @Transactional
    @Modifying
    @Query("DELETE FROM ConfMsg WHERE changeTime <= :messageTimeout")
    int clean(@Param("messageTimeout") long messageTimeout);
}
