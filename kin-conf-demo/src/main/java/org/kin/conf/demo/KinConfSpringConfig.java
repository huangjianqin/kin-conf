package org.kin.conf.demo;

import org.kin.conf.client.KinConf;
import org.kin.conf.client.spring.BeanConfHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author huangjianqin
 * @date 2019/7/19
 */
@SpringBootConfiguration
@EnableAutoConfiguration
public class KinConfSpringConfig implements InitializingBean {
    private Logger logger = LoggerFactory.getLogger(KinConfSpringConfig.class);

    @Value("${kin.conf.appName}")
    private String appName;
    @Value("${kin.conf.diamondAddress}")
    private String diamondAddress;
    @Value("${kin.conf.env}")
    private String env;
    @Value("${kin.conf.mirrorFile}")
    private String mirrorFile;


    @Override
    public void afterPropertiesSet() {
        KinConf.init(appName, diamondAddress, env, mirrorFile, true);
    }

    @Bean
    public ConfBean confBean() {
        return new ConfBean();
    }

    @Bean
    public BeanConfHandler beanConfHandler() {
        return new BeanConfHandler();
    }

    @Override
    public String toString() {
        return "KinConfSpringConfig{" +
                "appName='" + appName + '\'' +
                ", diamondAddress='" + diamondAddress + '\'' +
                ", env='" + env + '\'' +
                ", mirrorFile='" + mirrorFile + '\'' +
                '}';
    }
}
