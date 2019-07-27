package org.kin.conf.client.exception;

/**
 * @author huangjianqin
 * @date 2019/7/8
 */
public class ConfNotExistException extends RuntimeException {
    public ConfNotExistException(String key) {
        super("config key [" + key + "] does not exist");
    }
}
