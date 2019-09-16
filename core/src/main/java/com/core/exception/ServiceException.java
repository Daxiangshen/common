package com.core.exception;


import com.core.constant.ExceptionEnum;

public class ServiceException extends RuntimeException {

    private Integer code;

    public ServiceException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public ServiceException(String message) {
        super(message);
        this.code = ExceptionEnum.SYSTEM_EXCEPTION.getCode();
    }

    public ServiceException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMsg());
        this.code = exceptionEnum.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
