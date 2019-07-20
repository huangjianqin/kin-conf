package org.kin.conf.center.dao;

import java.io.Serializable;
import java.util.List;

/**
 * @author huangjianqin
 * @date 2019/7/12
 */
public interface BaseDao<E> {
    List<E> getAll();

    E get(Serializable id);

    boolean insert(E e);

    boolean update(E e);

    boolean delete(E e);
}
