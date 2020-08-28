package com.app.test;

import android.content.Context;
import android.net.Uri;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public enum EncryptMold {
    MD5("MD5"),
    SHA1("SHA-1"),
    SHA224("SHA-224"),
    SHA256("SHA-256"),
    SHA384("SHA-384"),
    SHA512("SHA-512");

    String algorithm;

    EncryptMold(java.lang.String algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * hash加密模板
     *
     * @param data      数据
     * @param algorithm 加密算法
     * @return 密文字节数组
     */
    private static byte[] hashTemplate(byte[] data, String algorithm) {
        if (data == null || data.length <= 0)
            return null;
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 关闭IO
     *
     * @param closeables closeables
     */
    private static void closeIO(Closeable... closeables) {
        if (closeables == null)
            return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 加密算法
     *
     * @return 密文
     */
    public byte[] encrypt(byte[] data) {
        return hashTemplate(data, algorithm);
    }

    /**
     * 加密算法
     *
     * @return 密文
     */
    public byte[] encrypt(String data) {
        return hashTemplate(data.getBytes(), algorithm);
    }

    /**
     * 加密
     *
     * @return 16进制密文
     */
    public String encryptToHex(byte[] data) {
        return ConvertUtils.bytes2Hex(encrypt(data));
    }

    /**
     * 加密
     *
     * @return 16进制密文
     */
    public String encryptToHex(String data) {
        return ConvertUtils.bytes2Hex(encrypt(data));
    }

    /**
     * 加密文件
     *
     * @param file 文件
     * @return 文件的校验码
     */
    public byte[] encrypt(File file) {
        if (file == null || !file.exists())
            return null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            return encrypt(fis);
        } catch (Exception e) {
            return null;
        } finally {
            closeIO(fis);
        }
    }

    /**
     * 加密文件
     *
     * @param file 文件
     * @return 文件的校验码
     */
    public byte[] encrypt(Context context, Uri file) {
        if (file == null)
            return null;
        InputStream fis = null;
        try {
            fis = context.getContentResolver().openInputStream(file);
            return encrypt(fis);
        } catch (Exception e) {
            return null;
        } finally {
            closeIO(fis);
        }
    }

    /**
     * 加密文件
     *
     * @param inputStream 文件流
     * @return 文件的校验码
     */
    public byte[] encrypt(InputStream inputStream) {
        DigestInputStream digestInputStream = null;
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            digestInputStream = new DigestInputStream(inputStream, md);
            byte[] buffer = new byte[10 * 1024];
            while (true) {
                if (!(digestInputStream.read(buffer) > 0))
                    break;
            }
            md = digestInputStream.getMessageDigest();
            return md.digest();
        } catch (Exception e) {
            return null;
        } finally {
            closeIO(inputStream, digestInputStream);
        }
    }

    public java.lang.String getAlgorithm() {
        return algorithm;
    }
}
