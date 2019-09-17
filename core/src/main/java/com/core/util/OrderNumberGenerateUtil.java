package com.core.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * <p>
 * 订单号生成工具类
 * <p/>
 *
 * @author GaoYang
 * @since 2019/5/8
 */
public class OrderNumberGenerateUtil {

    /**
     * 生成订单号
     *
     * @return 订单号
     */
    public synchronized static String generate() {
        return LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli() + Double.toString(Math.random()).substring(2, 5);
    }

}
