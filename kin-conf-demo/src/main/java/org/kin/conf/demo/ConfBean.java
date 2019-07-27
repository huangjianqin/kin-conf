package org.kin.conf.demo;

import org.kin.conf.client.domain.Conf;

/**
 * @author huangjianqin
 * @date 2019/7/20
 */
public class ConfBean {
    @Conf("a")
    private String a;

    @Override
    public String toString() {
        return "ConfBean{" +
                "a=" + a +
                '}';
    }
}
