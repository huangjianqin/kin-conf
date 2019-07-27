package org.kin.conf.client.domain;

import java.lang.reflect.Field;

/**
 * @author huangjianqin
 * @date 2019/7/19
 */
public class BeanConfWrapper {
    private String beanName;
    private Field confField;

    public BeanConfWrapper(String beanName, Field confField) {
        this.beanName = beanName;
        this.confField = confField;
    }

    public String getFieldName() {
        return confField.getName();
    }

    //getter
    public String getBeanName() {
        return beanName;
    }

    public Field getConfField() {
        return confField;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BeanConfWrapper that = (BeanConfWrapper) o;

        if (!beanName.equals(that.beanName)) return false;
        return confField.equals(that.confField);
    }

    @Override
    public int hashCode() {
        int result = beanName.hashCode();
        result = 31 * result + confField.hashCode();
        return result;
    }
}
