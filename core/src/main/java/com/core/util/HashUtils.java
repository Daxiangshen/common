package com.core.util;

import com.alibaba.fastjson.JSON;
import com.core.exception.ServiceException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 加密&解密
 */

public class HashUtils {

    public static final String SECRET_KEY = "1881176A18D7AA81";
    public static final String AESCBC_SECRET_KEY = "0880076B18D7EE81";
    public static final String AESCBC_INIT_VECTOR = "CB3EC842D7C69578";
    public static final String SIGN_KEY = "ADfj3kcadc2349akvm1CPFFCD84f";

    public static Object encryptObject(Object object, boolean isEncrypt) {

        // 如果加密需要加密
        if (isEncrypt) {
            String json = JSON.toJSONString(object);
            return HashUtils.encryptAESCBC(json);
        }
        return object;

    }

    public static String encryptString(String string, boolean isEncrypt) {
        if (!isEncrypt) {
            return string;
        }
        return HashUtils.encryptAESCBC(string);
    }

    public static String generateSign(Map<String, String[]> parameters, String key) {

        List<String> parameterList = new ArrayList<String>();
        for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
            for (String value : entry.getValue()) {
                parameterList.add(entry.getKey() + "=" + value);
            }
        }

        //降序排序
        Collections.sort(parameterList);

        //合并参数
        String parameterString = String.join("&", parameterList);
        System.out.println("before sign json:" + parameterString);

        //md5加密
        return HashUtils.MD(parameterString, SIGN_KEY);
    }

    public static String MD(String value, String salt) {

        if (!StringUtils.isEmpty(salt)) {
            value += salt;
        }

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(value.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            //32位加密
            return buf.toString();
            // 16位的加密
            //return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String MD5(String value, String salt) {
        if (!StringUtils.isEmpty(salt)) {
            value += salt;
        }
        try {
            MessageDigest md;
            md = MessageDigest.getInstance("MD5");
            md.update(value.getBytes(), 0, value.length());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public static String encryptAES(String value) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

            byte[] encrypted = cipher.doFinal(value.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(encrypted);

        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public static String encryptAESCBC(String value) {

        try {
            SecretKeySpec skeySpec = new SecretKeySpec(AESCBC_SECRET_KEY.getBytes("UTF-8"), "AES");
            IvParameterSpec iv = new IvParameterSpec(AESCBC_INIT_VECTOR.getBytes("UTF-8"));

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(encrypted);

        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }

    }

    public static String decryptAESCBC(String encrypted) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(AESCBC_SECRET_KEY.getBytes("UTF-8"), "AES");
            IvParameterSpec iv = new IvParameterSpec(AESCBC_INIT_VECTOR.getBytes("UTF-8"));

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            //去除换行　空格
            Pattern pattern = Pattern.compile("\\s*|\t|\r|\n");
            Matcher matcher = pattern.matcher(encrypted);
            encrypted = matcher.replaceAll("");

            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));

            return new String(original);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

}
