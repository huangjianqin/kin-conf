package org.kin.conf.center.controller;

import org.kin.conf.center.dao.ProjectDao;
import org.kin.conf.center.domain.CommonResponse;
import org.kin.conf.center.domain.Permission;
import org.kin.conf.center.entity.Project;
import org.kin.conf.center.utils.SpecialStrChecker;
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
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    private ProjectDao projectDao;

    @RequestMapping("/add")
    @ResponseBody
    @Permission
    public CommonResponse<String> add(HttpServletRequest request, Project project) {
        if (StringUtils.isBlank(project.getAppName())) {
            return CommonResponse.fail("应用名不能为空");
        }

        //不能包含特殊字符
        if (!SpecialStrChecker.check(project.getAppName())) {
            return CommonResponse.fail("应用名非法");
        }

        if (StringUtils.isBlank(project.getTitle())) {
            return CommonResponse.fail("应用标题不能为空");
        }

        Optional<Project> optional = projectDao.findById(project.getAppName());
        if (optional.isPresent()) {
            return CommonResponse.fail("应用已存在");
        }

        projectDao.save(project);

        return CommonResponse.success();
    }

    @RequestMapping("/delete")
    @ResponseBody
    @Permission
    public CommonResponse<String> delete(HttpServletRequest request, String appName) {
        if (StringUtils.isBlank(appName)) {
            return CommonResponse.fail("应用名不能为空");
        }

        Optional<Project> optional = projectDao.findById(appName);
        if (!optional.isPresent()) {
            return CommonResponse.fail("应用不存在");
        }

        Project dbProject = optional.get();

        projectDao.delete(dbProject);

        return CommonResponse.success();
    }

    @RequestMapping("/update")
    @ResponseBody
    @Permission
    public CommonResponse<String> update(HttpServletRequest request, Project project) {
        if (StringUtils.isBlank(project.getAppName())) {
            return CommonResponse.fail("应用名不能为空");
        }

        if (StringUtils.isBlank(project.getTitle())) {
            return CommonResponse.fail("应用标题不能为空");
        }

        Optional<Project> optional = projectDao.findById(project.getAppName());
        if (!optional.isPresent()) {
            return CommonResponse.fail("应用不存在");
        }

        Project dbProject = optional.get();
        dbProject.setTitle(project.getTitle());
        projectDao.save(dbProject);

        return CommonResponse.success();
    }
}
