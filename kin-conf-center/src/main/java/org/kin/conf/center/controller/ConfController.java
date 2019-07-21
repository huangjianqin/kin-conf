package org.kin.conf.center.controller;

import org.kin.conf.center.domain.CommonResponse;
import org.kin.conf.center.domain.ConfListResponse;
import org.kin.conf.center.domain.FindConfParams;
import org.kin.conf.center.entity.Conf;
import org.kin.conf.center.service.AdminService;
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
    private static String env = "test";

    @RequestMapping("/list")
    @ResponseBody
    public ConfListResponse list(HttpServletRequest request,
                                     int page,
                                     String appName) {
        return adminService.listConf(page, appName, env);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public CommonResponse<String> delete(HttpServletRequest request, String appName, String key){
        return adminService.delete(appName, env, key);
    }

    @RequestMapping("/add")
    @ResponseBody
    public CommonResponse<String> add(HttpServletRequest request, Conf conf){
        conf.setEnv(env);
        return adminService.add(conf);
    }

    @RequestMapping("/update")
    @ResponseBody
    public CommonResponse<String> update(HttpServletRequest request, Conf conf){
        conf.setEnv(env);
        return adminService.update(conf);
    }


    // ---------------------- rest api ----------------------
    @RequestMapping("/find")
    @ResponseBody
    public CommonResponse<Map<String, String>> getConfs(@RequestBody FindConfParams params){
        return adminService.getConfs(params.getAppName(), params.getEnv(), params.getKeys());
    }

    @RequestMapping("/monitor")
    @ResponseBody
    public DeferredResult<CommonResponse<String>> monitor(@RequestBody FindConfParams params) {
        return adminService.monitor(params.getAppName(), params.getEnv(), params.getKeys());
    }
}
