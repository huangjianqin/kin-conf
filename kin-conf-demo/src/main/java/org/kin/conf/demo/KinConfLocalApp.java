package org.kin.conf.demo;

import org.kin.conf.client.KinConf;

/**
 * @author huangjianqin
 * @date 2019-09-23
 */
public class KinConfLocalApp {
    public static void main(String[] args) {
        KinConf.init("data/mirror.properties");

        System.out.println(KinConf.get("a"));
        System.out.println(KinConf.getBoolean("b"));
        System.out.println(KinConf.getShort("c"));
        System.out.println(KinConf.getFloat("d"));
        System.out.println(KinConf.getInt("e"));
//        System.out.println(KinConf.getLong("f"));
    }
}
