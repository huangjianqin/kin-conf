package org.kin.conf.diamond.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import org.kin.conf.diamond.converter.PermissionDataTypeHandler;
import org.kin.conf.diamond.domain.Constants;
import org.kin.framework.utils.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huangjianqin
 * @date 2019/7/12
 */
@TableName(autoResultMap = true)
public class User implements Serializable {
    /** 账号 */
    private String account;
    /** 密码 */
    private String password;
    /** 用户名 */
    private String name;
    /** 权限：0-普通用户、1-管理员 */
    private int permission;
    /** 权限配置数据, 格式"appname#env#env01,appname#env02" */
    @TableField(typeHandler = PermissionDataTypeHandler.class)
    private Map<String, List<String>> permissionData = new HashMap<>();

    public boolean isUser() {
        return permission == Constants.USER;
    }

    public boolean isAdmin() {
        return permission == Constants.ADMIN;
    }

    public boolean hasPermission(String appName, String env) {
        List<String> envs;
        return permissionData.containsKey(appName) &&
                CollectionUtils.isNonEmpty((envs = permissionData.get(appName))) &&
                envs.contains(env);
    }

    public boolean addOrChangePermission(Map<String, List<String>> appName2Envs) {
        boolean change = false;
        for (Map.Entry<String, List<String>> entry : appName2Envs.entrySet()) {
            String appName = entry.getKey();
            List<String> envs = entry.getValue();
            if (CollectionUtils.isNonEmpty(envs)) {
                List<String> dbEnvs = permissionData.get(appName);
                if (CollectionUtils.isEmpty(dbEnvs)) {
                    dbEnvs = new ArrayList<>(envs);
                } else {
                    for (String env : envs) {
                        if (!dbEnvs.contains(env)) {
                            dbEnvs.add(env);
                        }
                    }
                }

                permissionData.put(appName, dbEnvs);
                change = true;
            }
        }

        return change;
    }

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

    public Map<String, List<String>> getPermissionData() {
        return permissionData;
    }

    public void setPermissionData(Map<String, List<String>> permissionData) {
        this.permissionData = permissionData;
    }
}
