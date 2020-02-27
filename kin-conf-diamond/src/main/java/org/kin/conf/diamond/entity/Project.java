package org.kin.conf.diamond.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author huangjianqin
 * @date 2019/7/12
 */
@Entity
@Table(name = "Project")
public class Project implements Serializable {
    @Id
    @Column(columnDefinition = "varchar(100) NOT NULL COMMENT 'AppName'")
    private String appName;
    @Column(columnDefinition = "varchar(100) NOT NULL COMMENT '项目名称'")
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
