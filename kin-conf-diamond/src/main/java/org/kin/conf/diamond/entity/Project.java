package org.kin.conf.diamond.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * @author huangjianqin
 * @date 2019/7/12
 */
@TableName(autoResultMap = true)
public class Project implements Serializable {
    /** AppName */
    private String appName;
    /** 项目名称 */
    private String title;

    //setter && getter
    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
