package org.kin.conf.diamond.interceptor;

import org.kin.conf.diamond.entity.User;
import org.kin.conf.diamond.service.UserService;
import org.kin.framework.web.interceptor.PermissionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author huangjianqin
 * @date 2019/7/26
 */
@Component
public class PermissionInterceptorImpl extends PermissionInterceptor {
    @Autowired
    private UserService userService;

    @Override
    public boolean customCheckLogin(HttpServletRequest request, HttpServletResponse response, boolean needAdmin) {
        User user = userService.getUser(request);
        if (user == null) {
            //TODO 跳转到登陆页面

            return false;
        }
        if (needAdmin && !user.isAdmin()) {
            throw new IllegalStateException("权限拦截");
        }
        return true;
    }
}
