package org.kin.conf.center;

import org.kin.conf.center.interceptor.CookiesInterceptor;
import org.kin.conf.center.interceptor.PermissionInterceptor;
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
public class KinConfCenterWebConfig extends SpringBootServletInitializer implements WebMvcConfigurer {
    @Autowired
    private PermissionInterceptor permissionInterceptor;
    @Autowired
    private CookiesInterceptor cookiesInterceptor;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        //加载配置类
        return builder.sources(KinConfCenterApplication.class);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(cookiesInterceptor).addPathPatterns("/**");
        registry.addInterceptor(permissionInterceptor).addPathPatterns("/**");
    }
}
