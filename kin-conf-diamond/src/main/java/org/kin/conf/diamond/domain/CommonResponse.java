package org.kin.conf.diamond.domain;

/**
 * @author huangjianqin
 * @date 2019/7/14
 */
public class CommonResponse<T> {
    public static final int SUCCESS_CODE = 200;
    public static final int FAIL_CODE = 500;

    private int code;
    private String msg;
    private T data;

    public CommonResponse() {
    }

    public CommonResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> CommonResponse<T> success() {
        return success(null);
    }

    public static <T> CommonResponse<T> success(String msg) {
        return success(msg, null);
    }

    public static <T> CommonResponse<T> success(T data) {
        return success("", data);
    }

    public static <T> CommonResponse<T> success(String msg, T data) {
        return new CommonResponse<>(SUCCESS_CODE, msg, data);
    }

    public static <T> CommonResponse<T> fail(String msg) {
        return fail(msg, null);
    }

    public static <T> CommonResponse<T> fail(String msg, T data) {
        return new CommonResponse<>(FAIL_CODE, msg, data);
    }

    //setter && getter
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
