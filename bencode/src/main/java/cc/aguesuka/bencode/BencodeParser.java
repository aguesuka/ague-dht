package cc.aguesuka.bencode;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

/**
 * @author :aguesuka
 * 2019/9/3 20:09
 */
class BencodeParser {
    BencodeMap rootMap = new BencodeMap();
    private ByteBuffer buff;
    private boolean isReadMap = true;
    private final Deque<IBencodeContainer<?>> stack = new ArrayDeque<>();

    BencodeParser() {
    }

    BencodeParser(ByteBuffer buff) {
        Objects.requireNonNull(buff);
        this.buff = buff;
    }

    BencodeMap parser() {
        try {


            stack.add(rootMap);
            if (byteFromBuffer() != BencodeToken.DICT) {
                throw ex("is not a bencode dict", null);
            }
            while (!stack.isEmpty()) {
                Object last = stack.getLast();
                if (isReadMap) {
                    String key = readStringOrEnd();
                    if (key != null) {
                        IBencode bencodeObject = readObject();
                        Objects.requireNonNull(bencodeObject);
                        ((BencodeMap) last).put(key, bencodeObject);
                    }
                } else {
                    IBencode bencode = readObject();
                    if (bencode != null) {
                        ((BencodeList) last).add(bencode);
                    }
                }
            }
            return rootMap;
        } catch (BencodeException e) {
            throw e;
        } catch (RuntimeException e) {
            throw ex(e.getMessage(), e);
        }
    }

    private void onEnd() {
        stack.removeLast();
        isReadMap = stack.peekLast() instanceof BencodeMap;
    }

    private IBencode readObject() {
        byte firstByte = byteFromBuffer();
        switch (firstByte) {
            case BencodeToken.END:
                onEnd();
                return null;
            case BencodeToken.DICT:
                BencodeMap bencodeMap = new BencodeMap();
                stack.addLast(bencodeMap);
                isReadMap = true;
                return bencodeMap;
            case BencodeToken.INT:
                return readIntegerAndCheckEnd(byteFromBuffer(), BencodeToken.END);
            case BencodeToken.LIST:
                BencodeList bencodeList = new BencodeList();
                stack.addLast(bencodeList);
                isReadMap = false;
                return bencodeList;
            default:
                return new BencodeByteArray(readBytesAndCheckEnd(firstByte));
        }
    }

    /**
     * 只有map的key是String
     * 在bencode中是 数字(较短,所以可以用int) + 冒号 + byte[] 类型;
     * 编码格式几乎都是ascii,不过为了保险依然使用utf-8
     *
     * @return String ,如果结束,则返回null
     */
    private String readStringOrEnd() {
        byte b = byteFromBuffer();
        if (b == BencodeToken.END) {
            onEnd();
            return null;
        } else {
            return new String(readBytesAndCheckEnd(b), Bencode.getCharset());
        }
    }

    private byte[] readBytesAndCheckEnd(byte firstByte) {
        BencodeInteger length = readIntegerAndCheckEnd(firstByte, BencodeToken.SPLIT);
        return byteArrayFromBuffer(length.intValue());
    }

    private BencodeInteger readIntegerAndCheckEnd(byte firstByte, byte endChar) {
        char c = '-';
        if (!Character.isDigit(firstByte) && firstByte != c) {
            throw ex("there must be digit or char '-' ", null);
        }
        char[] chars = new char[16];
        int index = 1;
        chars[0] = (char) firstByte;
        while (true) {
            byte codePoint = byteFromBuffer();
            if (!Character.isDigit(codePoint)) {
                if (codePoint != endChar) {
                    throw ex("there must be digit", null);
                }
                break;
            }
            // 扩容
            if (chars.length <= index) {
                char[] temp = new char[chars.length * 2];
                System.arraycopy(chars, 0, temp, 0, chars.length);
                chars = temp;
            }
            chars[index] = (char) codePoint;
            index++;
        }
        return new BencodeInteger(new String(chars, 0, index));
    }

    BencodeException ex(String msg, Throwable ex) {
        String m = "at position " + buff.position() + ":" + msg;
        return new BencodeException(rootMap, m, ex);
    }

    byte byteFromBuffer() {
        return buff.get();
    }


    byte[] byteArrayFromBuffer(int length) {
        byte[] bytes = new byte[length];
        buff.get(bytes);
        return bytes;
    }

}
