package org.kin.conf.diamond;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author huangjianqin
 * @date 2019/7/20
 */
@SpringBootApplication
@SpringBootConfiguration
@EnableCaching
@EnableJpaRepositories(basePackages = "org.kin.conf.diamond.dao")
@EntityScan(basePackages = "org.kin.conf.diamond.entity")
@ComponentScan(value = "org.kin.conf.diamond")
@EnableTransactionManagement(proxyTargetClass = true)
public class DiamondApplication {
    public static void main(String[] args) {
        SpringApplication.run(DiamondApplication.class);
    }
}
