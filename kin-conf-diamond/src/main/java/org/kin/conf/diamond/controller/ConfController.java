package org.kin.conf.diamond.controller;

import org.kin.conf.diamond.domain.ConfListResponse;
import org.kin.conf.diamond.domain.FindConfParams;
import org.kin.conf.diamond.domain.Permission;
import org.kin.conf.diamond.domain.WebResponse;
import org.kin.conf.diamond.entity.Conf;
import org.kin.conf.diamond.service.AdminService;
import org.kin.conf.diamond.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author huangjianqin
 * @date 2019/7/12
 */
@RestController
@RequestMapping("/conf")
public class ConfController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @RequestMapping("/list")
    @ResponseBody
    @Permission
    public ConfListResponse list(HttpServletRequest request,
                                 int page,
                                 String appName,
                                 String env) {
        return adminService.listConf(page, appName, env);
    }

    @RequestMapping("/delete")
    @ResponseBody
    @Permission
    public WebResponse<String> delete(HttpServletRequest request, String appName, String key, String env) {
        return adminService.delete(userService.getUser(request), appName, env, key);
    }

    @RequestMapping("/add")
    @ResponseBody
    @Permission
    public WebResponse<String> add(HttpServletRequest request, String env, Conf conf) {
        conf.setEnv(env);
        return adminService.add(userService.getUser(request), conf);
    }

    @RequestMapping("/update")
    @ResponseBody
    @Permission
    public WebResponse<String> update(HttpServletRequest request, String env, Conf conf) {
        conf.setEnv(env);
        return adminService.update(userService.getUser(request), conf);
    }


    // ---------------------- rest api ----------------------

    @RequestMapping(value = "/find")
    @ResponseBody
    public WebResponse<Map<String, String>> getConfs(@RequestBody FindConfParams params) {
        return adminService.getConfs(params.getAppName(), params.getEnv(), params.getKeys());
    }

    @RequestMapping("/monitor")
    @ResponseBody
    public DeferredResult<WebResponse<String>> monitor(@RequestBody FindConfParams params) {
        return adminService.monitor(params.getAppName(), params.getEnv(), params.getKeys());
    }
}
