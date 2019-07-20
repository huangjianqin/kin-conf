package org.kin.conf.center.dao;

import org.hibernate.Session;
import org.kin.framework.utils.ExceptionUtils;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * @author huangjianqin
 * @date 2019/7/12
 */
public abstract class BaseDaoSupport<E> extends HibernateDaoSupport implements BaseDao<E> {
    private Class<E> entityClass;

    public BaseDaoSupport() {
        Type type = getClass().getGenericSuperclass();
        if (!ParameterizedType.class.isAssignableFrom(type.getClass())) {
            throw new IllegalStateException("实现类必须使用范型");
        }

        entityClass = (Class<E>) ((ParameterizedType) type).getActualTypeArguments()[0];
    }

    public Session getSession() {
        return getSessionFactory().getCurrentSession();
    }

    @Override
    public List<E> getAll() {
        try {
            return getHibernateTemplate().loadAll(entityClass);
        } catch (Exception e) {
            ExceptionUtils.log(e);
        }

        return Collections.emptyList();
    }

    @Override
    public E get(Serializable id) {
        try {
            return getHibernateTemplate().get(entityClass, id);
        } catch (Exception e) {
            ExceptionUtils.log(e);
        }

        return null;
    }

    @Override
    public boolean insert(E e) {
        try {
            return ((int) getHibernateTemplate().save(e)) > 0;
        } catch (Exception ex) {
            ExceptionUtils.log(ex);
        }

        return false;
    }

    @Override
    public boolean update(E e) {
        try {
            getHibernateTemplate().saveOrUpdate(e);
            return true;
        } catch (Exception ex) {
            ExceptionUtils.log(ex);
        }

        return false;
    }

    @Override
    public boolean delete(E e) {
        try {
            getHibernateTemplate().delete(e);
            return true;
        } catch (Exception ex) {
            ExceptionUtils.log(ex);
        }

        return false;
    }
}
