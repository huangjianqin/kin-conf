package org.kin.conf.center.service;

import org.kin.conf.center.domain.CommonResponse;
import org.kin.conf.center.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author huangjianqin
 * @date 2019/7/26
 */
public interface UserService {
    CommonResponse<String> login(HttpServletResponse response, String account, String password);

    User isLogin(HttpServletRequest request);
}
