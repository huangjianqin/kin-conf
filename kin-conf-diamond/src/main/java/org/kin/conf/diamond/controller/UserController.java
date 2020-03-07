package org.kin.conf.diamond.controller;

import org.kin.conf.diamond.dao.EnvDao;
import org.kin.conf.diamond.dao.ProjectDao;
import org.kin.conf.diamond.dao.UserDao;
import org.kin.conf.diamond.domain.Permission;
import org.kin.conf.diamond.domain.WebResponse;
import org.kin.conf.diamond.entity.Env;
import org.kin.conf.diamond.entity.Project;
import org.kin.conf.diamond.entity.User;
import org.kin.conf.diamond.utils.SpecialStrChecker;
import org.kin.framework.utils.CollectionUtils;
import org.kin.framework.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author huangjianqin
 * @date 2019/7/13
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserDao userDao;
    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private EnvDao envDao;

    @RequestMapping("/add")
    @ResponseBody
    @Permission
    public WebResponse<String> add(HttpServletRequest request, User user) {
        if (StringUtils.isBlank(user.getAccount())) {
            return WebResponse.fail("账号不能为空");
        }

        if (StringUtils.isBlank(user.getName())) {
            return WebResponse.fail("名称不能为空");
        }

        if (StringUtils.isBlank(user.getPassword())) {
            return WebResponse.fail("密码不能为空");
        }
        //脱敏
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));

        Optional<User> optional = userDao.findById(user.getAccount());
        if (optional.isPresent()) {
            return WebResponse.fail("用户已存在");
        }

        userDao.save(user);

        return WebResponse.success();
    }

    @RequestMapping("/updateInfo")
    @ResponseBody
    @Permission
    public WebResponse<String> updateInfo(HttpServletRequest request, User user) {
        if (StringUtils.isBlank(user.getAccount())) {
            return WebResponse.fail("账号不能为空");
        }

        if (StringUtils.isBlank(user.getName())) {
            return WebResponse.fail("名称不能为空");
        }

        if (StringUtils.isBlank(user.getPassword())) {
            return WebResponse.fail("密码不能为空");
        }

        Optional<User> optional = userDao.findById(user.getAccount());
        if (!optional.isPresent()) {
            return WebResponse.fail("用户不存在");
        }

        //脱敏
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));

        User dbUser = optional.get();
        if (!dbUser.getPassword().equals(user.getPassword())) {
            return WebResponse.fail("密码不一致");
        }

        dbUser.setName(user.getName());

        userDao.save(dbUser);

        return WebResponse.success();
    }

    @RequestMapping("/updatePassword")
    @ResponseBody
    @Permission
    public WebResponse<String> updatePassword(HttpServletRequest request,
                                              String account,
                                              String oldPassword,
                                              String newPassword) {
        if (StringUtils.isBlank(account)) {
            return WebResponse.fail("账号不能为空");
        }

        if (StringUtils.isBlank(oldPassword)) {
            return WebResponse.fail("原密码不能为空");
        }

        if (StringUtils.isBlank(newPassword)) {
            return WebResponse.fail("新密码不能为空");
        }

        Optional<User> optional = userDao.findById(account);
        if (!optional.isPresent()) {
            return WebResponse.fail("用户不存在");
        }

        //脱敏
        oldPassword = DigestUtils.md5DigestAsHex(oldPassword.getBytes());
        newPassword = DigestUtils.md5DigestAsHex(newPassword.getBytes());

        User dbUser = optional.get();
        if (!dbUser.getPassword().equals(oldPassword)) {
            return WebResponse.fail("密码不一致");
        }

        dbUser.setPassword(newPassword);

        userDao.save(dbUser);

        return WebResponse.success();
    }

    @RequestMapping("/updatePermission")
    @ResponseBody
    @Permission
    public WebResponse<String> updatePermission(HttpServletRequest request,
                                                String account,
                                                String password,
                                                Map<String, List<String>> appName2Envs) {
        if (StringUtils.isBlank(account)) {
            return WebResponse.fail("账号不能为空");
        }

        if (StringUtils.isBlank(password)) {
            return WebResponse.fail("原密码不能为空");
        }

        if (CollectionUtils.isEmpty(appName2Envs)) {
            return WebResponse.fail("权限数据不能为空");
        }

        Optional<User> optional = userDao.findById(account);
        if (!optional.isPresent()) {
            return WebResponse.fail("用户不存在");
        }

        //脱敏
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        User dbUser = optional.get();
        if (!dbUser.getPassword().equals(password)) {
            return WebResponse.fail("密码不一致");
        }

        for (Map.Entry<String, List<String>> entry : appName2Envs.entrySet()) {
            String appName = entry.getKey();
            if (SpecialStrChecker.check(appName)) {
                Optional<Project> projectOptional = projectDao.findById(appName);
                if (projectOptional.isPresent()) {
                    List<String> envs = entry.getValue();
                    if (CollectionUtils.isNonEmpty(envs)) {
                        for (String env : envs) {
                            if (SpecialStrChecker.check(env)) {
                                Optional<Env> envOptional = envDao.findById(env);
                                if (!envOptional.isPresent()) {
                                    return WebResponse.fail(env + "环境不存在");
                                }

                                if (dbUser.hasPermission(appName, env)) {
                                    return WebResponse.fail("用户" + dbUser.getName() + "已经拥有" + appName + "应用" + env + "环境的权限");
                                }
                            } else {
                                return WebResponse.fail("环境名非法");
                            }
                        }
                    } else {
                        return WebResponse.fail("环境列表不能为空");
                    }
                } else {
                    return WebResponse.fail(appName + "应用不存在");
                }
            } else {
                return WebResponse.fail("应用名非法");
            }
        }

        dbUser.addOrChangePermission(appName2Envs);
        userDao.save(dbUser);
        return WebResponse.success();
    }
}
