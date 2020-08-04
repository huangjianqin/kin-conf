package org.kin.conf.demo;

import org.kin.conf.client.KinConf;
import org.kin.framework.concurrent.ExecutionContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @author huangjianqin
 * @date 2019/7/19
 */
@SpringBootApplication
public class KinConfSpringBootApplication {
    @Bean
    public ConfBean conf() {
        return new ConfBean();
    }

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(KinConfSpringBootApplication.class, args);

        System.out.println(KinConf.getAppName());
        System.out.println(KinConf.get("a"));
        System.out.println(KinConf.getBoolean("b"));
        System.out.println(KinConf.getShort("c"));
        System.out.println(KinConf.getFloat("d"));
        System.out.println(KinConf.getInt("e"));

        //测试合并请求
        int testThreadNum = 5;
        ExecutionContext executor = ExecutionContext.fix(testThreadNum, "test-");
        //镜像不存在的key
        for (int i = 0; i < testThreadNum; i++) {
            int finalI = i;
            executor.execute(() -> {
                for (int j = finalI * 100; j < finalI * 110; j++) {
                    KinConf.get(j + "");
                }
            });
        }
        executor.shutdown();

        System.out.println(context.getBean(ConfBean.class));
        System.exit(0);
    }
}
