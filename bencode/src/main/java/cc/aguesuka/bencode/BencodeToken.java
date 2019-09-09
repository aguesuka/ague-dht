package cc.aguesuka.bencode;

/**
 * @author :yangmingyuxing
 * 2019/9/3 17:23
 */
class BencodeToken {
    static final byte INT = 'i';
    static final byte DICT = 'd';
    static final byte END = 'e';
    static final byte LIST = 'l';
    static final byte SPLIT = ':';
    static final String NULL = "";
}
