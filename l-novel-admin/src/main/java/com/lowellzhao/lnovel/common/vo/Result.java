package com.lowellzhao.lnovel.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 前端控制器返回封装类
 *
 * @author lowellzhao
 * @since 2022-05-26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    /**
     * 成功
     */
    public static final int SUCCESS = 0;
    /**
     * 失败
     */
    public static final int FAIL = -1;
    /**
     * 未登录
     */
    public static final int NOT_LOGIN = 1000;

    private int code;

    private String msg;

    private String userMsg;

    private T data;


    public static <T> Result<T> success(String msg, String userMsg, T t) {
        return new Result<>(Result.SUCCESS, msg, userMsg, t);
    }

    public static <T> Result<T> success(T t) {
        return new Result<>(Result.SUCCESS, "SUCCESS", "请求成功", t);
    }

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> error(String msg, String userMsg, T t) {
        return new Result<>(Result.FAIL, msg, userMsg, t);
    }

    public static <T> Result<T> error(String msg, String userMsg) {
        return new Result<>(Result.FAIL, msg, userMsg, null);
    }

    public static <T> Result<T> error(String userMsg) {
        return new Result<>(Result.FAIL, "", userMsg, null);
    }

    public static <T> Result<T> error(T t) {
        return new Result<>(Result.FAIL, "", "", t);
    }

    public static <T> Result<T> error() {
        return new Result<>(Result.FAIL, "", "服务异常，稍后再试", null);
    }

    public static <T> Result<T> notLoginError() {
        return new Result<>(Result.NOT_LOGIN, "FAILED", "请登录后再进行操作!", null);
    }


}
