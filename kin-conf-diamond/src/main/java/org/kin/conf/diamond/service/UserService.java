package org.kin.conf.diamond.service;

import org.kin.conf.diamond.domain.CommonResponse;
import org.kin.conf.diamond.entity.User;

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
