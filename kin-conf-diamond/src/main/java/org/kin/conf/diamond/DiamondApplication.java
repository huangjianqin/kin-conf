package org.kin.conf.diamond;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author huangjianqin
 * @date 2019/7/20
 */
@SpringBootApplication
@EnableCaching
@EnableTransactionManagement(proxyTargetClass = true)
public class DiamondApplication {
    public static void main(String[] args) {
        SpringApplication.run(DiamondApplication.class);
    }
}
