package org.kin.conf.center.controller;

import org.kin.conf.center.domain.CommonResponse;
import org.kin.conf.center.domain.CookieKey;
import org.kin.conf.center.domain.Permission;
import org.kin.conf.center.service.UserService;
import org.kin.conf.center.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author huangjianqin
 * @date 2019/7/22
 */
@RestController
@RequestMapping("/center")
public class ConfCenterController {
    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    @ResponseBody
    public CommonResponse<String> login(HttpServletRequest request, HttpServletResponse response, String account, String password) {
        return userService.login(response, account, password);
    }

    @RequestMapping("/logout")
    @ResponseBody
    @Permission
    public CommonResponse<String> login(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.remove(request, response, CookieKey.LOGIN_IDENTITY);
        return CommonResponse.success();
    }
}
