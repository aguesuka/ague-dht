package cc.aguesuka.bencode;

/**
 * @author :yangmingyuxing
 * 2019/6/30 17:25
 */
interface IBencodeContainer<T> {
    /**
     * get 强转为 byte[]
     *
     * @param key 键
     * @return byte[]
     */
    default byte[] getByteArray(T key) {
        IBencode bencode = getBencode(key);
        return bencode == null ? null : ((BencodeByteArray) bencode).getBytes();
    }

    /**
     * get 强转为Integer
     *
     * @param key 键
     * @return int
     */
    default BencodeInteger getInteger(T key) {
        return (BencodeInteger) getBencode(key);
    }

    /**
     * get 强转为BencodeMap
     *
     * @param key 键
     * @return BencodeMap
     */
    default BencodeMap getBencodeMap(T key) {
        return (BencodeMap) getBencode(key);
    }

    /**
     * get 强转为 BencodeList
     *
     * @param key 键
     * @return BencodeList
     */
    @SuppressWarnings("unused")
    default BencodeList getBencodeList(T key) {
        return (BencodeList) getBencode(key);
    }

    /**
     * get 强转为
     *
     * @param key 键
     * @return BencodeList
     */
    IBencode getBencode(T key);

    /**
     * 获得String
     *
     * @param key k键
     * @return String
     */
    default String getString(T key) {
        byte[] value = getByteArray(key);
        return value == null ? null : new String(value, Bencode.getCharset());
    }
}
