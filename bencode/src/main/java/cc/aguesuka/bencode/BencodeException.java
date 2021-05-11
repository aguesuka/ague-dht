package cc.aguesuka.bencode;

/**
 * @author :aguesuka
 * 2019/9/1 23:15
 */
public final class BencodeException extends RuntimeException {
    private final Object data;

    BencodeException(Object data, Throwable cause) {
        super(cause);
        this.data = data;
    }

    BencodeException(Object data, String msg, Throwable cause) {
        super(msg, cause);
        this.data = data;
    }


    BencodeException(Object data, String message) {
        super(message);
        this.data = data;
    }

    @SuppressWarnings("unused")
    public Object getData() {
        return data;
    }
}
