package org.kin.conf.diamond.controller;

import org.kin.conf.diamond.dao.EnvDao;
import org.kin.conf.diamond.domain.Permission;
import org.kin.conf.diamond.domain.WebResponse;
import org.kin.conf.diamond.entity.Env;
import org.kin.conf.diamond.utils.SpecialStrChecker;
import org.kin.framework.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author huangjianqin
 * @date 2019/7/13
 */
@RestController
@RequestMapping("/env")
public class EnvController {
    @Autowired
    private EnvDao envDao;

    @RequestMapping("/add")
    @ResponseBody
    @Permission
    public WebResponse<String> add(HttpServletRequest request, Env env) {
        if (StringUtils.isBlank(env.getEnv())) {
            return WebResponse.fail("环境名不能为空");
        }

        //不能包含特殊字符
        if (!SpecialStrChecker.check(env.getEnv())) {
            return WebResponse.fail("环境名非法");
        }

        if (StringUtils.isBlank(env.getDescription())) {
            return WebResponse.fail("环境描述不能为空");
        }

        Optional<Env> optional = envDao.findById(env.getEnv());
        if (optional.isPresent()) {
            return WebResponse.fail("环境已存在");
        }

        envDao.save(env);

        return WebResponse.success();
    }

    @RequestMapping("/delete")
    @ResponseBody
    @Permission
    public WebResponse<String> delete(HttpServletRequest request, String env) {
        if (StringUtils.isBlank(env)) {
            return WebResponse.fail("环境不能为空");
        }

        Optional<Env> optional = envDao.findById(env);
        if (!optional.isPresent()) {
            return WebResponse.fail("环境不存在");
        }

        Env dbEnv = optional.get();

        envDao.delete(dbEnv);

        return WebResponse.success();
    }

    @RequestMapping("/update")
    @ResponseBody
    @Permission
    public WebResponse<String> update(HttpServletRequest request, Env env) {
        if (StringUtils.isBlank(env.getEnv())) {
            return WebResponse.fail("环境不能为空");
        }

        if (StringUtils.isBlank(env.getDescription())) {
            return WebResponse.fail("环境描述不能为空");
        }

        Optional<Env> optional = envDao.findById(env.getEnv());
        if (!optional.isPresent()) {
            return WebResponse.fail("环境不存在");
        }

        Env dbEnv = optional.get();
        dbEnv.setDescription(env.getDescription());
        dbEnv.setTorder(env.getTorder());

        envDao.save(dbEnv);

        return WebResponse.success();
    }
}
