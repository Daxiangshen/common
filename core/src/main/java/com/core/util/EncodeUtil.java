package com.core.util;

import java.security.MessageDigest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * 加密工具类
 */
public class EncodeUtil {
    private static String getEncode(String password) {
        MessageDigest md5;
        try {
            // 生成一个MD5加密计算摘要

            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        char[] charArray = password.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuilder hexValue = new StringBuilder();
        for (byte md5Byte : md5Bytes) {
            int val = ((int) md5Byte) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }

            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    public static String encode(String password) {
        String salt = "innjoy";
        return getEncode(salt + password) + ":" + salt;
    }


    public static void main(String[] args) {

        ConcurrentHashMap<String, Double> data = new ConcurrentHashMap<>();
        data.put("10_", 1.0);
        data.put("40_", 1.0);
        data.put("20_", 1.0);
        data.put("10_1", 1.0);
        Stream<Map.Entry<String, Double>> w = data.entrySet().stream().filter(k ->
                k.getKey().contains("10_")
        );
    }
}
