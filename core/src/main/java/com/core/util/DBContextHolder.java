package com.core.util;

public class DBContextHolder {
    // 对当前线程的操作-线程安全的
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    private static final ThreadLocal<String> hbaseContext = new ThreadLocal<>();

    // 调用此方法，切换数据源
    public static void setDataSource(String dataSource) {
        contextHolder.set(dataSource);
    }

    // 获取数据源
    public static String getDataSource() {
        return contextHolder.get();
    }

    // 删除数据源
    public static void clearDataSource() {
        contextHolder.remove();
    }

    public static void setHBaseDataSource(String dataSource) {
        hbaseContext.set(dataSource);
    }

    // 获取数据源
    public static String getHBaseDataSource() {
        return hbaseContext.get();
    }

    // 删除数据源
    public static void clearHBaseDataSource() {
        hbaseContext.remove();
    }
}
