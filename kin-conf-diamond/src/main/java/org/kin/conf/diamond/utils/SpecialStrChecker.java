package org.kin.conf.diamond.utils;

/**
 * @author huangjianqin
 * @date 2019/7/22
 */
public class SpecialStrChecker {
    /**
     * env和appName不能包含特殊字符
     */
    public static boolean check(String s) {
        return !s.contains("#") && !s.contains(",");
    }

}
