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
@Table(name = "Env")
public class Env implements Serializable {
    @Id
    @Column(columnDefinition = "varchar(100) NOT NULL COMMENT 'Env'")
    private String env;
    @Column(columnDefinition = "varchar(100) NOT NULL COMMENT '环境描述'")
    private String desc;
    @Column(columnDefinition = "tinyint(4) NOT NULL DEFAULT '0' COMMENT '显示排序'")
    private int order;


    //setter && getter
    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
