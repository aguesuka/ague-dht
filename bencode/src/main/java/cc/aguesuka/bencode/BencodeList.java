package cc.aguesuka.bencode;

import java.util.ArrayList;

/**
 * @author :yangmingyuxing
 * 2019/6/30 16:58
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public final class BencodeList extends ArrayList<IBencode> implements IBencode, IBencodeContainer<Integer> {
    public void addLong(long i) {
        add(new BencodeInteger(i));
    }

    public void addByteArray(byte[] bytes) {
        add(new BencodeByteArray(bytes));
    }

    @Override
    public IBencode getBencode(Integer key) {
        return get(key);
    }
}
