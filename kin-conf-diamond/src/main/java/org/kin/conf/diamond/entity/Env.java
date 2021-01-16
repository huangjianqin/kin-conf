package org.kin.conf.diamond.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * @author huangjianqin
 * @date 2019/7/12
 */
@TableName(autoResultMap = true)
public class Env implements Serializable {
    /** 项目应用环境 */
    private String env;
    /** 环境描述 */
    private String description;
    /** 显示排序 */
    private int torder;

    //setter && getter
    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTorder() {
        return torder;
    }

    public void setTorder(int torder) {
        this.torder = torder;
    }
}
