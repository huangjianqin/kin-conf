package org.kin.conf.client.utils;

import org.kin.conf.client.domain.BeanConfWrapper;
import org.kin.conf.client.exception.ConfValueTypeErrorException;
import org.kin.framework.utils.ClassUtils;
import org.kin.framework.utils.ExceptionUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;

/**
 * @author huangjianqin
 * @date 2019/7/19
 */
public class BeanConfRefreshHelper {
    /**
     * 刷新bean 有@Conf注解的Field
     */
    public static void refreshBeanField(ApplicationContext context, BeanConfWrapper wrapper, String value, Object bean) {
        if (bean == null) {
            bean = context.getBean(wrapper.getBeanName());
        }
        if (bean == null) {
            return;
        }

        String fieldName = wrapper.getFieldName();

        //分装类, 容易使用, 给field赋值
        BeanWrapper beanWrapper = new BeanWrapperImpl(bean);

        PropertyDescriptor propertyDescriptor = null;
        PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();
        if (org.kin.framework.utils.CollectionUtils.isNonEmpty(propertyDescriptors)) {
            for (PropertyDescriptor item : propertyDescriptors) {
                if (fieldName.equals(item.getName())) {
                    propertyDescriptor = item;
                }
            }
        }


        if (propertyDescriptor != null && propertyDescriptor.getWriteMethod() != null) {
            //可以设置
            beanWrapper.setPropertyValue(fieldName, value);
        } else {
            //通过反射field设置
            Object finalBean = bean;
            ReflectionUtils.doWithFields(finalBean.getClass(), field -> {
                if (fieldName.equals(field.getName())) {
                    Object valueObj = ClassUtils.string2Obj(field, value);

                    field.setAccessible(true);
                    try {
                        try {
                            field.set(finalBean, valueObj);        // support mult data types
                        } catch (IllegalAccessException e) {
                            ExceptionUtils.log(e);
                        } catch (Exception e) {
                            throw new ConfValueTypeErrorException(field.getType(), value);
                        }
                    } finally {
                        field.setAccessible(false);
                    }
                }
            });
        }

    }
}
