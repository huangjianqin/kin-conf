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
    /**
     * 登陆
     *
     * @param response response
     * @param account  账号
     * @param password 密码
     * @return 登陆结果
     */
    CommonResponse<String> login(HttpServletResponse response, String account, String password);

    /**
     * 获取会话登陆中的用户
     *
     * @param request request
     * @return 会话登陆中的用户
     */
    User getUser(HttpServletRequest request);
}
