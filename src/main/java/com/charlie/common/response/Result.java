package com.charlie.common.response;

import lombok.Data;

@Data
public class Result<T> {
    private boolean success;
    private String message;
    private T data;

    // 成功且有資料
    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.setSuccess(true);
        r.setData(data);
        r.setMessage("成功");
        return r;
    }

    // 成功但無資料
    public static Result<Void> success() {
        Result<Void> r = new Result<>();
        r.setSuccess(true);
        r.setMessage("成功");
        return r;
    }

    // 失敗
    public static <T> Result<T> fail(String message) {
        Result<T> r = new Result<>();
        r.setSuccess(false);
        r.setMessage(message);
        return r;
    }
}
