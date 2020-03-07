package org.kin.conf.diamond.controller;

import org.kin.conf.diamond.domain.CookieKey;
import org.kin.conf.diamond.domain.Permission;
import org.kin.conf.diamond.domain.WebResponse;
import org.kin.conf.diamond.service.UserService;
import org.kin.conf.diamond.utils.CookieUtils;
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
@RequestMapping("/diamond")
public class DiamondController {
    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    @ResponseBody
    public WebResponse<String> login(HttpServletRequest request, HttpServletResponse response, String account, String password) {
        return userService.login(response, account, password);
    }

    @RequestMapping("/logout")
    @ResponseBody
    @Permission
    public WebResponse<String> login(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.remove(request, response, CookieKey.LOGIN_IDENTITY);
        return WebResponse.success();
    }
}
