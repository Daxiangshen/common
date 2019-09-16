package com.core.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "返回说明")
public class ResultBody<T> {
    /**
     * 状态码
     */
    @ApiModelProperty(value = "返回状态码；200:成功")
    private Integer code;
    /**
     * 消息
     */
    @ApiModelProperty(value = "描述信息")
    private String msg;

    /**
     * 参数信息
     */
    private T data;


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
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

    @ApiModelProperty(value = "成功标识；true：成功；false:失败")
    public boolean isSuccess() {
        return this.code == 200;
    }
}

