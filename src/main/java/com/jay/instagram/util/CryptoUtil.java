package com.jay.instagram.util;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

/**
 * 密码加密类
 */
@Slf4j
public class CryptoUtil {

    private static final String DEFAULT_CHARSET = "UTF-8";

    private static final String salt = "ins";

    /**
     * BASE64加密
     *
     * @param bytes
     *            an array of byte.
     * @return a {@link java.lang.String} object.
     */
    public static String encodeBASE64(final byte[] bytes) {
        return new String(Base64.encodeBase64String(bytes));
    }

    /**
     * BASE64加密
     *
     * @param str
     *            a {@link java.lang.String} object.
     * @param charset
     *            a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String encodeBASE64(final String str, String charset) {
        if (str == null) {
            return null;
        }
        try {
            byte[] bytes = str.getBytes(charset);
            return encodeBASE64(bytes);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * BASE64加密,默认UTF-8
     *
     * @param str
     *            a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String encode(final String str) {
        String newStr = salt + str;
        return encodeBASE64(newStr, DEFAULT_CHARSET);
    }

    /**
     * BASE64解密,默认UTF-8
     *
     * @param str
     *            a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String decode(String str) {
        String retStr = decodeBASE64(str, DEFAULT_CHARSET).substring(salt.length());
        return retStr;
    }

    /**
     * BASE64解密
     *
     * @param str
     *            a {@link java.lang.String} object.
     * @param charset
     *            字符编码
     * @return a {@link java.lang.String} object.
     */
    public static String decodeBASE64(String str, String charset) {
        try {
            byte[] bytes = str.getBytes(charset);
            return new String(Base64.decodeBase64(bytes));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String a = "123456";
//        String key = "yang测试";
        System.out.println(a);
        String m = encode(a);
        System.out.println(m);
        String n = decode(m);
        System.out.println(n);
    }
}