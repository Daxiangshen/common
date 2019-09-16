package com.core.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;
import java.util.UUID;

public class KeyUtil {
    private static int keyLenth = 16;

    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyzQWERTYUIOPASDFGHJKLZXCVBNM0123456789&-#$%*!(){}";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成六位数的验证码
     */
    public static String getContent() {
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            int r = random.nextInt(10); //每次随机出一个数字（0-9）
            code.append(r);  //把每次随机出的数字拼在一起
        }
        System.out.println(code);
        return code.toString();
    }

    public static void main(String[] args) {
        String clientId = UUID.randomUUID().toString().replace("-", "");
        System.out.println("clientId              " + clientId);
        String signKey = RandomStringUtils.randomAscii(keyLenth);
        System.out.println("signKey               " + signKey);
        String aesKey = RandomStringUtils.randomAscii(keyLenth);
        System.out.println("aesKey                " + aesKey);
        String aesIv = RandomStringUtils.randomAscii(keyLenth);
        System.out.println("aesIv                 " + aesIv);
        String s1 = RandomStringUtils.randomAlphanumeric(keyLenth);
        System.out.println("s1                    " + s1);
        String s2 = RandomStringUtils.randomAlphabetic(keyLenth);
        System.out.println("s2                    " + s2);
        String s3 = KeyUtil.getRandomString(keyLenth);
        System.out.println("s3                    " + s3);
    }

}
