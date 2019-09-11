package cc.aguesuka.bencode;

import org.junit.Assert;
import org.junit.Test;

import java.security.SecureRandom;

/**
 * @author :yangmingyuxing
 * 2019/9/1 10:31
 */
public class BencodeListTest {

    private SecureRandom secureRandom = new SecureRandom();

    @Test
    public void addLong() {
    }

    @Test
    public void addByteArray() {
    }

    @Test
    public void getByteArray() {

        byte[] bytes = new byte[0];
        secureRandom.nextBytes(bytes);
        BencodeList test = new BencodeList();
        test.add(null);
        test.addByteArray(bytes);
        assert null == test.getByteArray(0);
        Assert.assertArrayEquals(bytes, test.getByteArray(1));
    }

    @Test
    public void getInteger() {

    }

    @Test
    public void getBencodeMap() {
    }

    @Test
    public void getBencodeList() {
    }
}