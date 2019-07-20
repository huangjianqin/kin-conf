package org.kin.conf.demo;

import org.kin.conf.core.KinConf;
import org.kin.conf.core.spring.BeanConfHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author huangjianqin
 * @date 2019/7/19
 */
@Configuration
public class KinConfSpringConfig implements InitializingBean {
    private Logger logger = LoggerFactory.getLogger(KinConfSpringConfig.class);

    @Value("${kin.conf.centerAddress}")
    private String centerAddress;
    @Value("${kin.conf.env}")
    private String env;
    @Value("${kin.conf.mirrorFile}")
    private String mirrorFile;


    @Override
    public void afterPropertiesSet() throws Exception {
        KinConf.init(centerAddress, env, mirrorFile);
    }

    @Bean
    public ConfBean confBean() {
        return new ConfBean();
    }

    @Bean
    public BeanConfHandler beanConfHandler() {
        return new BeanConfHandler();
    }
}
