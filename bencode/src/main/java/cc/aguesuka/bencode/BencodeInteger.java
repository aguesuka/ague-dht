package cc.aguesuka.bencode;

/**
 * 整数类型,实际取值范围和java int 不同,不可变对象
 *
 * @author :aguesuka
 * 2019/6/30 17:02
 */
public final class BencodeInteger extends Number implements IBencode {
    private final long value;

    BencodeInteger(String val) {
        value = Long.parseLong(val);
    }

    public BencodeInteger(long val) {
        value = val;
    }


    @Override
    public int intValue() {
        return (int) value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return value;
    }

    @Override
    public String toString() {
        return Long.toString(value);
    }

    @Override
    public double doubleValue() {
        return (double) value;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BencodeInteger)) {
            return false;
        }
        BencodeInteger that = (BencodeInteger) o;
        return this.value == that.value;
    }
}
