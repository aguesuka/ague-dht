package cc.aguesuka.bencode;

import java.util.LinkedHashMap;

/**
 * @author :aguesuka
 * 2019/6/30 16:57
 */
public final class BencodeMap extends LinkedHashMap<String, IBencode> implements IBencode, IBencodeContainer<String> {
    public void putByteArray(String key, byte[] value) {
        put(key, new BencodeByteArray(value));
    }

    public void putString(String key, String value) {
        putByteArray(key, value.getBytes(Bencode.getCharset()));
    }

    @SuppressWarnings("unused")
    public void putLong(String key, long value) {
        put(key, new BencodeInteger(value));
    }

    @Override
    public IBencode getBencode(String key) {
        return get(key);
    }
}
