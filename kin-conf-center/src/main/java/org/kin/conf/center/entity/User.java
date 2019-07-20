package org.kin.conf.center.entity;

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
@Table(name = "User")
public class User implements Serializable {
    @Id
    @Column(columnDefinition = "varchar(100) NOT NULL COMMENT '账号'")
    private String account;
    @Column(columnDefinition = "varchar(100) NOT NULL COMMENT '密码'")
    private String password;
    @Column(columnDefinition = "varchar(100) NOT NULL COMMENT '用户名'")
    private String name;
    @Column(columnDefinition = "tinyint(4) NOT NULL DEFAULT '0' COMMENT '权限：0-普通用户、1-管理员'")
    private int permission;
    @Column(columnDefinition = "varchar(1000) DEFAULT NULL COMMENT '权限配置数据'")
    private String permission_data;

    //setter && getter
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public String getPermission_data() {
        return permission_data;
    }

    public void setPermission_data(String permission_data) {
        this.permission_data = permission_data;
    }
}
