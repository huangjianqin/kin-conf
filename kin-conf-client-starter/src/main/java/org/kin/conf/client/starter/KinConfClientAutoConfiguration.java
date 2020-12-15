package org.kin.conf.client.starter;

import org.kin.conf.client.KinConf;
import org.kin.conf.client.spring.BeanConfHandler;
import org.kin.conf.client.starter.config.KinConfClientProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

import javax.annotation.PostConstruct;

/**
 * @author huangjianqin
 * @date 2020/8/4
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({KinConfClientProperties.class})
@ConditionalOnClass(KinConf.class)
@ConditionalOnProperty(value = "kin.conf.client.enabled", havingValue = "true")
public class KinConfClientAutoConfiguration {
    private ConfigurableEnvironment environment;

    public KinConfClientAutoConfiguration(ConfigurableEnvironment environment) {
        this.environment = environment;
    }

    @Autowired
    private KinConfClientProperties clientProperties;

    @PostConstruct
    public void init() {
        KinConf.init(
                environment.getProperty("spring.application.name", "kin-conf-client"),
                clientProperties.getDiamondAddress(),
                clientProperties.getEnv(),
                clientProperties.getMirrorFile(),
                true);
    }

    @Bean
    public BeanConfHandler beanConfHandler() {
        return new BeanConfHandler();
    }

}
