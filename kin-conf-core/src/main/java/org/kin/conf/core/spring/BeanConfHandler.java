package org.kin.conf.core.spring;

import org.kin.conf.core.KinConf;
import org.kin.conf.core.domain.BeanConfWrapper;
import org.kin.conf.core.domain.Conf;
import org.kin.conf.core.listener.impl.BeanConfRefresher;
import org.kin.conf.core.utils.BeanConfRefreshHelper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

/**
 * @author huangjianqin
 * @date 2019/7/20
 */
@Component
public class BeanConfHandler extends InstantiationAwareBeanPostProcessorAdapter implements ApplicationContextAware {
    private ApplicationContext context;
    private BeanConfRefresher beanConfRefresh;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
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
