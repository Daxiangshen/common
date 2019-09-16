package com.core.util;


import com.core.constant.ExceptionEnum;
import com.core.model.ResultBody;

public class ResultUtil {
    /**
     * 返回成功，传入返回体具体出參
     */
    public static <T> ResultBody success(T t) {
        ResultBody result = new ResultBody();
        result.setCode(200);
        result.setMsg("success");
        result.setData(t);
        return result;
    }

    /**
     * 提供给部分不需要出參的接口
     */
    public static ResultBody success() {
        return success(null);
    }

    /**
     * 自定义错误信息
     */
    public static ResultBody error(Integer code, String msg) {
        ResultBody result = new ResultBody();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    /**
     * 请求失败，返回错误码及错误信息
     *
     * @param code 错误码
     * @param msg  错误信息
     * @return
     */
    public static ResultBody error(Integer code, String msg, Object data) {
        ResultBody result = new ResultBody();
        result.setCode(code);
        result.setData(data);
        result.setMsg(msg);
        return result;
    }

    /**
     * 返回异常信息，在已知的范围内
     */
    public static ResultBody error(ExceptionEnum exceptionEnum) {
        ResultBody result = new ResultBody();
        result.setCode(exceptionEnum.getCode());
        result.setMsg(exceptionEnum.getMsg());
        return result;
    }

    /**
     * 返回异常信息，在已知的范围内
     */
    public static ResultBody error(ExceptionEnum exceptionEnum, Object data) {
        ResultBody result = new ResultBody();
        result.setCode(exceptionEnum.getCode());
        result.setMsg(exceptionEnum.getMsg());
        result.setData(data);
        return result;
    }

}