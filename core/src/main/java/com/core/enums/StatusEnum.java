package com.core.enums;

public enum StatusEnum {
    SUCCESS(0, "成功"),
    ERROR(-1000, "系统异常"),
    FAILED(-1, "操作失败"),
    STREAM_DATA(1, "数据传输"),
    STREAM_ADD(2, "新增数据"),
    STREAM_UPDATE(3, "更新数据"),
    STREAM_DELETE(4, "删除数据");

    private Integer code;
    private String message;

    private StatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public boolean isSuccess() {
        return this.code == 0;
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toString() {
        return this.code.toString();
    }
}