package com.core.model;

public class StreamsVo<T> {
    // 操作类型
    private Integer operation;
    // 消息体
    private T payload;

    public StreamsVo() {
    }

    public Integer getOperation() {
        return this.operation;
    }

    public T getPayload() {
        return this.payload;
    }

    public void setOperation(Integer operation) {
        this.operation = operation;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public String toString() {
        return "StreamsVo(operation=" + this.getOperation() + ", payload=" + this.getPayload() + ")";
    }
}