package org.kin.conf.demo;

import org.kin.conf.client.KinConf;
import org.kin.framework.concurrent.SimpleThreadFactory;
import org.kin.framework.concurrent.ThreadManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
        System.out.println(KinConf.getFloat("d"));
        System.out.println(KinConf.getInt("e"));

        //测试合并请求
        int testThreadNum = 5;
        ThreadManager executor = new ThreadManager(
                new ThreadPoolExecutor(testThreadNum, testThreadNum, 60, TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(), new SimpleThreadFactory("test-")));
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
    }
}
