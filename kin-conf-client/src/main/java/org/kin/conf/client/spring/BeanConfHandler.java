package org.kin.conf.client.spring;

import org.kin.conf.client.KinConf;
import org.kin.conf.client.domain.BeanConfWrapper;
import org.kin.conf.client.domain.Conf;
import org.kin.conf.client.listener.impl.BeanConfRefresher;
import org.kin.conf.client.utils.BeanConfRefreshHelper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Nonnull;

/**
 * @author huangjianqin
 * @date 2019/7/20
 */
public class BeanConfHandler extends InstantiationAwareBeanPostProcessorAdapter implements ApplicationContextAware {
    private ApplicationContext context;
    private BeanConfRefresher beanConfRefresh;

    @Override
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
        this.beanConfRefresh = new BeanConfRefresher(this.context);
    }

    /**
     * 处理@Conf 注解
     */
    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        ReflectionUtils.doWithFields(bean.getClass(), field -> {
            if (field.isAnnotationPresent(Conf.class)) {
                Conf confAnnotation = field.getAnnotation(Conf.class);

                String confKey = confAnnotation.value();
                String confValue = KinConf.get(confKey, confAnnotation.defaultValue());

                BeanConfWrapper wrapper = new BeanConfWrapper(beanName, field);
                BeanConfRefreshHelper.refreshBeanField(context, wrapper, confValue, bean);

                // watch
                if (confAnnotation.watch()) {
                    beanConfRefresh.watch(confKey, wrapper);
                }

            }
        });

        return super.postProcessAfterInstantiation(bean, beanName);
    }
}
