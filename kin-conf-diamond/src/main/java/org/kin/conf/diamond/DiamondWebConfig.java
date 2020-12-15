package org.kin.conf.diamond;

import org.kin.conf.diamond.interceptor.PermissionInterceptorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author huangjianqin
 * @date 2019/7/26
 */
@Configuration
public class DiamondWebConfig extends SpringBootServletInitializer implements WebMvcConfigurer {
    @Autowired
    private PermissionInterceptorImpl permissionInterceptorImpl;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        //加载配置类
        return builder.sources(DiamondApplication.class);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(permissionInterceptorImpl).addPathPatterns("/**");
    }
}
