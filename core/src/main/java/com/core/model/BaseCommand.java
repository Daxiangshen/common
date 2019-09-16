package com.core.model;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;


public class BaseCommand implements Serializable {
    private static final long serialVersionUID = 13453453L;
    /**
     * 分页参数
     */
    @ApiModelProperty(value = "分页参数,单页数量")
    private Integer pageSize = 10;
    @ApiModelProperty(value = "分页参数,当前页码")
    private Integer pageCurrent = 1;
    @ApiModelProperty(hidden = true)
    private Integer pageIndex;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageCurrent() {
        return pageCurrent;
    }

    public void setPageCurrent(Integer pageCurrent) {
        this.pageCurrent = pageCurrent;
    }

    public Integer getPageIndex() {
        return (pageCurrent-1)*pageSize;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }
}
