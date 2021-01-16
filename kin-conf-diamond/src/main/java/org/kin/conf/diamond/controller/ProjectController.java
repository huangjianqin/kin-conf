package org.kin.conf.diamond.controller;

import org.kin.conf.diamond.dao.ProjectDao;
import org.kin.conf.diamond.domain.Permission;
import org.kin.conf.diamond.domain.WebResponse;
import org.kin.conf.diamond.entity.Project;
import org.kin.conf.diamond.utils.SpecialStrChecker;
import org.kin.framework.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author huangjianqin
 * @date 2019/7/13
 */
@RestController
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    private ProjectDao projectDao;

    @RequestMapping("/add")
    @ResponseBody
    @Permission
    public WebResponse<String> add(HttpServletRequest request, Project project) {
        if (StringUtils.isBlank(project.getAppName())) {
            return WebResponse.fail("应用名不能为空");
        }

        //不能包含特殊字符
        if (!SpecialStrChecker.check(project.getAppName())) {
            return WebResponse.fail("应用名非法");
        }

        if (StringUtils.isBlank(project.getTitle())) {
            return WebResponse.fail("应用标题不能为空");
        }

        Project dbProject = projectDao.selectById(project.getAppName());
        if (Objects.nonNull(dbProject)) {
            return WebResponse.fail("应用已存在");
        }

        projectDao.insert(project);

        return WebResponse.success();
    }

    @RequestMapping("/delete")
    @ResponseBody
    @Permission
    public WebResponse<String> delete(HttpServletRequest request, String appName) {
        if (StringUtils.isBlank(appName)) {
            return WebResponse.fail("应用名不能为空");
        }

        Project dbProject = projectDao.selectById(appName);
        if (Objects.isNull(dbProject)) {
            return WebResponse.fail("应用不存在");
        }

        projectDao.deleteById(dbProject);

        return WebResponse.success();
    }

    @RequestMapping("/update")
    @ResponseBody
    @Permission
    public WebResponse<String> update(HttpServletRequest request, Project project) {
        if (StringUtils.isBlank(project.getAppName())) {
            return WebResponse.fail("应用名不能为空");
        }

        if (StringUtils.isBlank(project.getTitle())) {
            return WebResponse.fail("应用标题不能为空");
        }

        Project dbProject = projectDao.selectById(project.getAppName());
        if (Objects.isNull(dbProject)) {
            return WebResponse.fail("应用不存在");
        }

        dbProject.setTitle(project.getTitle());
        projectDao.updateById(dbProject);

        return WebResponse.success();
    }
}
