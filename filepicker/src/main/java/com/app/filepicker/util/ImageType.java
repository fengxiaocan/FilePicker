package com.app.filepicker.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ImageType {

    /**
     * 获取文件的mimeType
     *
     * @param filename
     * @return
     */
    public static String getMimeType(String filename) {
        try {
            String mimeType = readType(filename);
            return String.format("image/%s", mimeType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "image/file";
    }

    public static String getMimeType(File file) {
        try {
            String mimeType = readType(file);
            return String.format("image/%s", mimeType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "image/file";
    }

    public static String getImageType(File file) {
        try {
            return readType(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "file";
    }


    public static String getImageType(String filename) {
        try {
            return readType(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "file";
    }

    public static boolean isImage(String filename) {
        try {
            String mimeType = readType(filename);
            return "jpeg".equals(mimeType) || "png".equals(mimeType) || "gif".equals(mimeType) ||
                   "webp".equals(mimeType) || "bmp".equals(mimeType) || "ico".equals(mimeType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 读取文件类型
     *
     * @param filename
     * @return
     * @throws IOException
     */
    private static String readType(String filename) throws IOException {
        return readType(new File(filename));
    }

    public static boolean isGif(File file) {
        FileInputStream fis = null;
        try {
            if (!file.exists() || file.isDirectory() || file.length() < 8) {
                return false;
            }
            fis = new FileInputStream(file);
            byte[] bufHeaders = readInputStreamAt(fis, 0, 8);
            return isGIF(bufHeaders);
        } catch (Exception e) {

        } finally {
            closeIo(fis);
        }
        return false;
    }

    public static boolean isGif(String filename) {
        return isGif(new File(filename));
    }

    public static boolean isPng(File file) {
        FileInputStream fis = null;
        try {
            if (!file.exists() || file.isDirectory() || file.length() < 8) {
                return false;
            }
            fis = new FileInputStream(file);
            byte[] bufHeaders = readInputStreamAt(fis, 0, 8);
            return isPNG(bufHeaders);
        } catch (Exception e) {

        } finally {
            closeIo(fis);
        }
        return false;
    }

    public static boolean isPng(String filename) {
        return isPng(new File(filename));
    }

    public static boolean isWebp(File file) {
        FileInputStream fis = null;
        try {
            if (!file.exists() || file.isDirectory() || file.length() < 8) {
                return false;
            }
            fis = new FileInputStream(file);
            byte[] bufHeaders = readInputStreamAt(fis, 0, 8);
            return isWEBP(bufHeaders);
        } catch (Exception e) {
        } finally {
            closeIo(fis);
        }
        return false;
    }

    public static boolean isWebp(String filename) {
        return isWebp(new File(filename));
    }


    public static boolean isBmp(File file) {
        FileInputStream fis = null;
        try {
            if (!file.exists() || file.isDirectory() || file.length() < 8) {
                return false;
            }
            fis = new FileInputStream(file);
            byte[] bufHeaders = readInputStreamAt(fis, 0, 8);
            return isBMP(bufHeaders);
        } catch (Exception e) {
        } finally {
            closeIo(fis);
        }
        return false;
    }

    public static boolean isBmp(String filename) {
        return isBmp(new File(filename));
    }

    public static boolean isIco(File file) {
        FileInputStream fis = null;
        try {
            if (!file.exists() || file.isDirectory() || file.length() < 8) {
                return false;
            }
            fis = new FileInputStream(file);
            byte[] bufHeaders = readInputStreamAt(fis, 0, 8);
            return isICON(bufHeaders);
        } catch (Exception e) {
        } finally {
            closeIo(fis);
        }
        return false;
    }

    public static boolean isIco(String filename) {
        return isIco(new File(filename));
    }

    public static boolean isJpeg(File file) {
        FileInputStream fis = null;
        try {
            if (!file.exists() || file.isDirectory() || file.length() < 8) {
                return false;
            }
            fis = new FileInputStream(file);
            byte[] bufHeaders = readInputStreamAt(fis, 0, 8);
            if (isJPEGHeader(bufHeaders)) {
                long skiplength = file.length() - 2 - 8; //第一次读取时已经读了8个byte,因此需要减掉
                byte[] bufFooters = readInputStreamAt(fis, skiplength, 2);
                if (isJPEGFooter(bufFooters)) {
                    return true;
                }
            }
        } catch (Exception e) {
        } finally {
            closeIo(fis);
        }
        return false;
    }

    public static boolean isJpeg(String filename) {
        return isJpeg(new File(filename));
    }

    /**
     * @param file
     * @return
     * @throws IOException
     */
    private static String readType(File file) throws IOException {
        FileInputStream fis = null;
        try {
            if (!file.exists() || file.isDirectory() || file.length() < 8) {
                throw new IOException("the file [" + file.getAbsolutePath() + "] is not image !");
            }
            fis = new FileInputStream(file);
            byte[] bufHeaders = readInputStreamAt(fis, 0, 8);
            if (isJPEGHeader(bufHeaders)) {
                long skiplength = file.length() - 2 - 8; //第一次读取时已经读了8个byte,因此需要减掉
                byte[] bufFooters = readInputStreamAt(fis, skiplength, 2);
                if (isJPEGFooter(bufFooters)) {
                    return "jpeg";
                }
            }
            if (isPNG(bufHeaders)) {
                return "png";
            }
            if (isGIF(bufHeaders)) {
                return "gif";
            }
            if (isWEBP(bufHeaders)) {
                return "webp";
            }
            if (isBMP(bufHeaders)) {
                return "bmp";
            }
            if (isICON(bufHeaders)) {
                return "ico";
            }
            throw new IOException("the image's format is unkown!");

        } catch (Exception e) {
            throw e;
        } finally {
            closeIo(fis);
        }
    }

    private static void closeIo(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
            }
        }
    }

    private static boolean isBMP(byte[] buf) {
        byte[] markBuf = "BM".getBytes();  //BMP图片文件的前两个字节
        return compare(buf, markBuf);
    }

    private static boolean isICON(byte[] buf) {
        byte[] markBuf = {0, 0, 1, 0, 1, 0, 32, 32};
        return compare(buf, markBuf);
    }

    private static boolean isWEBP(byte[] buf) {
        byte[] markBuf = "RIFF".getBytes(); //WebP图片识别符
        return compare(buf, markBuf);
    }

    private static boolean isGIF(byte[] buf) {
        byte[] markBuf = "GIF89a".getBytes(); //GIF识别符
        if (compare(buf, markBuf)) {
            return true;
        }
        markBuf = "GIF87a".getBytes(); //GIF识别符
        if (compare(buf, markBuf)) {
            return true;
        }
        return false;
    }


    private static boolean isPNG(byte[] buf) {
        byte[] markBuf = {(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A}; //PNG识别符
        // new String(buf).indexOf("PNG")>0 //也可以使用这种方式
        return compare(buf, markBuf);
    }

    private static boolean isJPEGHeader(byte[] buf) {
        byte[] markBuf = {(byte) 0xff, (byte) 0xd8}; //JPEG开始符
        return compare(buf, markBuf);
    }

    private static boolean isJPEGFooter(byte[] buf)//JPEG结束符
    {
        byte[] markBuf = {(byte) 0xff, (byte) 0xd9};
        return compare(buf, markBuf);
    }


    /**
     * 标示一致性比较
     *
     * @param buf     待检测标示
     * @param markBuf 标识符字节数组
     * @return 返回false标示标示不匹配
     */
    private static boolean compare(byte[] buf, byte[] markBuf) {
        for (int i = 0; i < markBuf.length; i++) {
            byte b = markBuf[i];
            byte a = buf[i];

            if (a != b) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param fis        输入流对象
     * @param skiplength 跳过位置长度
     * @param length     要读取的长度
     * @return 字节数组
     * @throws IOException
     */
    private static byte[] readInputStreamAt(FileInputStream fis, long skiplength, int length)
            throws IOException
    {
        byte[] buf = new byte[length];
        fis.skip(skiplength);
        fis.read(buf, 0, length);
        return buf;
    }
}
