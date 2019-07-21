package org.kin.conf.center.controller;

import org.kin.conf.center.dao.EnvDao;
import org.kin.conf.center.domain.CommonResponse;
import org.kin.conf.center.entity.Env;
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
    public CommonResponse<String> add(HttpServletRequest request, Env env){
        if(StringUtils.isBlank(env.getEnv())){
            return CommonResponse.fail("环境不能为空");
        }

        if(StringUtils.isBlank(env.getDescription())){
            return CommonResponse.fail("环境描述不能为空");
        }

        Optional<Env> optional = envDao.findById(env.getEnv());
        if (optional.isPresent()) {
            return CommonResponse.fail("环境不存在");
        }

        envDao.save(env);

        return CommonResponse.success();
    }
}
