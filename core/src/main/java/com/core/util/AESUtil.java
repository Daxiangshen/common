package com.core.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Jaye AES 是一种可逆加密算法，对用户的敏感信息加密处理
 * 对原始数据进行AES加密后，在进行Base64编码转化；
 * 正确
 */
public class AESUtil {
    /*已确认
    * 加密用的Key 可以用26个字母和数字组成
    * 此处使用AES-128-CBC加密模式，key需要为16位。
    */
    private static String ENCODING_FORMAT = "UTF-8";

    // 算法名称
    private static final String KEY_ALGORITHM = "AES";
    // 加解密算法/模式/填充方式
    private static final String algorithmStr = "AES/CBC/PKCS5Padding";



    // 加密
    public static String encrypt(String sSrc, String skey, String sIv) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithmStr);
        byte[] raw = skey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, KEY_ALGORITHM);
        IvParameterSpec iv = new IvParameterSpec(sIv.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes(ENCODING_FORMAT));
        return Base64.encodeBase64String(encrypted);
    }

    // 解密
    public static String decrypt(String sSrc, String skey, String sIv) throws Exception {
        try {
            byte[] raw = skey.getBytes(ENCODING_FORMAT);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(algorithmStr);
            IvParameterSpec iv = new IvParameterSpec(sIv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = Base64.decodeBase64(sSrc);
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original, ENCODING_FORMAT);
        } catch (Exception ex) {
            return null;
        }
    }

}