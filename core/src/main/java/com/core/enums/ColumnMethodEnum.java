package com.core.enums;

public enum ColumnMethodEnum {
    like(1),
    gt(2),
    lt(3),
    between(4),
    equal(5),
    isnull(6),
    notnull(7);
    private int code;

    private ColumnMethodEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
