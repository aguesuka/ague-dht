package cc.aguesuka.bencode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * java object to bencode byte array
 * todo Circular reference check
 *
 * @author yangmingyuxing
 */
class BencodeEncoder {
    private Object bencode;
    private ByteArrayOutputStream outputStream;

    BencodeEncoder() {
        this.outputStream = new ByteArrayOutputStream();
    }

    private void write(byte[] bytes) {
        try {
            writeByteArray(bytes);
        } catch (Exception e) {
            throw new BencodeException(bencode, e);
        }
    }

    void writeByteArray(byte[] bytes) throws IOException {
        outputStream.write(bytes);
    }

    void writeByte(byte b) {
        outputStream.write(b);
    }

    byte[] getResult() {
        return outputStream.toByteArray();
    }


    private void putInteger(Number i) {
        writeByte(BencodeToken.INT);
        write(i.toString().getBytes(Bencode.getCharset()));
        writeByte(BencodeToken.END);
    }

    private void putString(String s) {
        putByteArray(s.getBytes(Bencode.getCharset()));
    }

    private void putMap(Map<?, ?> map) {
        writeByte(BencodeToken.DICT);
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            putString((String) entry.getKey());
            putBencode(entry.getValue());
        }
        writeByte(BencodeToken.END);
    }

    private void putList(List<?> list) {
        writeByte(BencodeToken.LIST);
        for (Object o : list) {
            putBencode(o);
        }
        writeByte(BencodeToken.END);
    }


    void putBencode(Object o) {
        this.bencode = o;
        if (o == null) {
            putString(BencodeToken.NULL);
            return;
        }
        if (o instanceof Map) {
            putMap((Map) o);
        } else if (o instanceof BencodeByteArray) {
            putByteArray(((BencodeByteArray) o).getBytes());
        } else if (o instanceof Number) {
            putInteger((Number) o);
        } else if (o instanceof List) {
            putList((List) o);
        } else if (o instanceof byte[]) {
            putByteArray(((byte[]) o));
        } else {
            throw new BencodeException(bencode, "can not encode:" + o.getClass());
        }
    }

    private void putByteArray(byte[] bytes) {
        write(Integer.toString(bytes.length).getBytes(Bencode.getCharset()));
        writeByte(BencodeToken.SPLIT);
        write(bytes);
    }


}