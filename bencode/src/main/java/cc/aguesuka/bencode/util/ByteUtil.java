package cc.aguesuka.bencode.util;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;

/**
 * @author aguesuka
 */
public final class ByteUtil {

    /**
     * 比较两个byte数组代表的大小.可理解为256进制正整数的大小比较
     *
     * @param a byte 数组1
     * @param b byte 数组2
     * @return {@link Comparator#compare(Object, Object)}
     */
    public static int compareBytes(byte[] a, byte[] b) {
        Comparator<byte[]> comparator = Comparator.comparingInt(
                (byte[] bytes) -> bytes.length);
        for (int i = 0; i < a.length; i++) {
            int j = i;
            comparator = comparator.thenComparingInt((byte[] bytes) -> bytes[j] & 0xff);
        }
        return comparator.compare(a, b);
    }


    /**
     * 判断一个ByteBuffer剩余的bytes能否转为utf-8
     *
     * @param byteBuffer 封装byte array的buffer
     * @return ByteBuffer剩余的bytes能否转为utf-8
     */
    public static boolean isUtf8(ByteBuffer byteBuffer) {
        int ii = 0b1100_0000;
        int io = 0b1000_0000;
        int i8 = 0b1111_1111;
        if (!byteBuffer.hasRemaining()) {
            return true;
        }
        while (byteBuffer.hasRemaining()) {
            int b = byteBuffer.get() & i8;
            if ((b & io) != io) {
                if (b < 32) {
                    return false;
                }
                continue;
            }
            if ((b & ii) == io) {
                return false;
            }
            for (b <<= 1; (b & io) == io; b <<= 1) {

                if (!byteBuffer.hasRemaining() || (ii & byteBuffer.get()) != io) {
                    return false;
                }
            }
        }
        return true;
    }


    public static byte[] sha1(byte[] bytes) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            return messageDigest.digest(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}

