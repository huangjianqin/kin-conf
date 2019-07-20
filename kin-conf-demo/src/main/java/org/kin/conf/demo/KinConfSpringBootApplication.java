package org.kin.conf.demo;

import org.kin.conf.core.KinConf;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * @author huangjianqin
 * @date 2019/7/19
 */
@SpringBootApplication
public class KinConfSpringBootApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(KinConfSpringConfig.class, args);

        System.out.println(KinConf.get("a"));
        System.out.println(KinConf.getBoolean("b"));
        System.out.println(KinConf.getShort("c"));
        System.out.println(KinConf.getInt("d"));
        System.out.println(KinConf.getLong("e"));
        System.out.println(KinConf.getFloat("f"));
        System.out.println(KinConf.getDouble("g"));

        System.out.println(context.getBean(ConfBean.class));
    }
}
