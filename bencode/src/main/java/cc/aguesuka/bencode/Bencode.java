package cc.aguesuka.bencode;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * http://www.bittorrent.org/beps/bep_0003.html#bencoding
 * <p>
 * bencode是bit-torrent协议的常用编码,消息和种子文件中都有用到.
 * 本类提供IBencode对象和byte数组/ByteBuffer的互相转换
 * {@link Bencode#toBytes(Object)}
 * {@link Bencode#writeToBuffer(IBencode, ByteBuffer)}
 * {@link Bencode#parse(ByteBuffer)}
 * {@link Bencode#parse(byte[])}
 * <p>
 * bencode 有四种类型
 * <p>
 * INTEGER 整数 {@link BencodeInteger}
 * Integers are represented by an 'i' followed by the number in base 10 followed by an 'e'.
 * For example i3e corresponds to 3 and i-3e corresponds to -3. Integers have no size limitation.
 * i-0e is invalid. All encodings with a leading zero, such as i03e,
 * are invalid, other than i0e, which of course corresponds to 0.
 * <p>
 * STRING 对应Java中的byte[]而非字符串; {@link BencodeByteArray} 字符串可以转为BencodeByteArray反之则不一定
 * Strings are length-prefixed base ten followed by a colon and the string. For example 4:spam corresponds to 'spam'.
 * <p>
 * Lists are encoded as an 'l' followed by their elements (also bencoded) followed by an 'e'.
 * For example l4:spam4:eggse corresponds to ['spam', 'eggs'].
 * LIST 数组 {@link BencodeList}
 * <p>
 * Dictionaries are encoded as a 'd' followed by a list of alternating keys and their corresponding values followed by an 'e'.
 * For example, d3:cow3:moo4:spam4:eggse corresponds to {'cow': 'moo', 'spam': 'eggs'}
 * and d4:spaml1:a1:bee corresponds to {'spam': ['a', 'b']}.
 * Keys must be strings and appear in sorted order (sorted as raw strings, not alphanumerics).
 * DICT 字典 {@link BencodeMap} map的key是字符串
 * <p>
 * bencode 没有定义null,但是在处理时不做校验,所以解析成对象时,map的最后一个value可能为null,编码为bencode时,null视为空字符串
 *
 * @author :aguesuka
 * 2019/6/26 17:47
 */
public final class Bencode {

    private static final Charset charset = StandardCharsets.UTF_8;

    static Charset getCharset() {
        return charset;
    }

    /**
     * 将 ByteBuff 对象转 BencodeMap ,只转换第一个 BencodeMap 并将 ByteBuff 的 offset 置为第一个map的结尾.
     *
     * @param buff byte buff 对象
     * @return BencodeMap
     */
    public static BencodeMap parse(ByteBuffer buff) {
        try {
            return new BencodeParser(buff).parser();
        } catch (BencodeException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new BencodeException(null, e);
        }
    }

    /**
     * 将 ByteBuff 对象转 BencodeMap ,只转换第一个 BencodeMap 并将 ByteBuff 的 offset 置为第一个map的结尾.
     *
     * @param bytes byte[] 对象
     * @return BencodeMap
     */
    @SuppressWarnings("WeakerAccess")
    public static BencodeMap parse(byte[] bytes) {
        try {
            return new BencodeParser() {
                int position = 0;

                @Override
                BencodeException ex(String msg, Throwable ex) {
                    String m = "at position " + position + ":" + msg;
                    return new BencodeException(rootMap, m, ex);
                }

                @Override
                byte byteFromBuffer() {
                    return bytes[position++];
                }

                @Override
                byte[] byteArrayFromBuffer(int length) {
                    byte[] result = new byte[length];
                    System.arraycopy(bytes, position, result, 0, length);
                    position += length;
                    return result;
                }
            }.parser();
        } catch (BencodeException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new BencodeException(null, e);
        }

    }

    /**
     * 将IBencode对象转为byte数组
     *
     * @param o IBencode对象
     * @return byte 数组
     */
    static byte[] toBytes(Object o) {
        BencodeEncoder bencodeEncode = new BencodeEncoder();
        bencodeEncode.putBencode(o);
        return bencodeEncode.getResult();
    }

    static ByteBuffer writeToBuffer(IBencode o, ByteBuffer buffer) {
        BencodeEncoder bencodeEncode = new BencodeEncoder() {
            @Override
            void writeByteArray(byte[] bytes) {
                buffer.put(bytes);
            }

            @Override
            void writeByte(byte b) {
                buffer.put(b);
            }

            @Override
            byte[] getResult() {
                return null;
            }
        };
        bencodeEncode.putBencode(o);
        return buffer;
    }

}
