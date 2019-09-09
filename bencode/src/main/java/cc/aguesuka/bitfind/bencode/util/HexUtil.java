package cc.aguesuka.bitfind.bencode.util;

import java.util.Objects;

/**
 * 十六进制工具类 oracle jdk 有,open jdk 需要自己写
 *
 * @author :yangmingyuxing
 * 2019/7/11 23:25
 */
public class HexUtil {


    private static final char[] B2H = new char[16];
    private static final byte[] H2B = new byte[128];

    static {
        byte num = 0;
        char h0 = '0';
        char h9 = '9';
        char h10 = 'A';
        char h16 = 'F';
        byte illegal = -1;
        for (char hex = h0; hex <= h9; hex++) {
            B2H[num++] = hex;
        }
        for (char i = h10; i <= h16; i++) {
            B2H[num++] = i;
        }

        for (int i = 0; i < H2B.length; i++) {
            H2B[i] = illegal;
        }
        num = 0;
        for (int i = h0; i <= h9; i++) {
            H2B[i] = num++;
        }
        for (int i = h10; i <= h16; i++) {
            H2B[i] = num++;
        }
        num = 0xA;
        h10 = 'a';
        h16 = 'f';
        for (int i = h10; i <= h16; i++) {
            H2B[i] = num++;
        }

    }

    /**
     * 十六进制转byte数组
     *
     * @param hex 十六进制字符串
     * @return byte数组
     */
    public static byte[] decode(String hex) {
        Objects.requireNonNull(hex);
        char[] chars = hex.toCharArray();
        if ((chars.length & 1) != 0) {
            throw new IllegalArgumentException();
        }
        byte[] result = new byte[chars.length / 2];
        for (int i = 0; i < result.length; i++) {
            byte b0 = h2b(chars[i * 2]);
            byte b1 = h2b(chars[i * 2 + 1]);
            result[i] = ((byte) (b0 << 4 | b1));
        }
        return result;

    }

    private static byte h2b(char c) {
        if (c > H2B.length) {
            throw new IllegalArgumentException();
        }
        byte b = H2B[(byte) c];
        if (b == -1) {
            throw new IllegalArgumentException();
        }
        return b;
    }

    /**
     * byte数组转十六进制
     *
     * @param bytes byte数组
     * @return 十六进制字符串
     */
    public static String encode(byte[] bytes) {
        Objects.requireNonNull(bytes);
        char[] b = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            byte bi = bytes[i];
            b[i * 2] = B2H[(bi & 0xff) >>> 4];
            b[i * 2 + 1] = B2H[bi & 0b0000_1111];
        }
        return new String(b);
    }
}
