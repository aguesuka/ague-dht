package cc.aguesuka.btfind.util;

import cc.aguesuka.bencode.util.HexUtil;
import org.junit.Assert;
import org.junit.Test;

import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * @author :yangmingyuxing
 * 2019/9/9 12:33
 */
public class Bep0042ImplTest {

    @Test
    public void changeId() throws UnknownHostException {
        testChangeId("124.31.75.21", "5fbfbff10c5d6a4ec8a88e4c6ab4c28b95eee401", true);
        testChangeId("21.75.31.124", "5a3ce9c14e7a08645677bbd1cfe7d8f956d53256", true);
        testChangeId("65.23.51.170", "a5d43220bc8f112a3d426c84764f8c2a1150e616", true);
        testChangeId("84.124.73.14", "1b0321dd1bb1fe518101ceef99462b947a01ff41", true);
        testChangeId("43.213.53.83", "e56f6cbf5b7c4be0237986d5243b87aa6d51305a", true);

        testChangeId("43.213.53.83", "e56f6cbf5b7c4be0237986d5243b87aa6d51305f", false);
        testChangeId("43.213.53.82", "e56f6cbf5b7c4be0237986d5243b87aa6d51305a", false);
    }

    public void testChangeId(String host, String id, boolean isEquals) throws UnknownHostException {
        byte[] src = HexUtil.decode(id);
        byte[] result = Bep0042Impl.changeId(host, src);
        if (isEquals) {
            Assert.assertArrayEquals(src, result);
        } else {
            assert !Arrays.equals(src, result);
        }
    }
}