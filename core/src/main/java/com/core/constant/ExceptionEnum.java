package com.core.constant;


public enum ExceptionEnum {
    SYSTEM_EXCEPTION(500, "系统异常"),
    INVALID_PARAMETER(999, "无效参数"),
    USER_FAILED(1021, "获取用户失败");

    private Integer code;
    private String msg;

    ExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
