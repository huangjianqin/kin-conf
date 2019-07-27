package org.kin.conf.client.exception;

/**
 * @author huangjianqin
 * @date 2019/7/20
 */
public class ConfValueTypeErrorException extends RuntimeException {
    public ConfValueTypeErrorException(Class<?> claxx, String confValue) {
        super("目标类型: " + claxx.getName() + ", 配置值: " + confValue);
    }
}
