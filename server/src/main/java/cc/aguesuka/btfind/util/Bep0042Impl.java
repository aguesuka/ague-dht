package cc.aguesuka.btfind.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.zip.CRC32C;

/**
 * http://www.bittorrent.org/beps/bep_0042.html
 *
 * @author :aguesuka
 * 2019/9/9 12:30
 */
class Bep0042Impl {
    private static final byte[] V4_MASK = {0x03, 0x0f, 0x3f, (byte) 0xff};

    /**
     * http://www.bittorrent.org/beps/bep_0042.html
     *
     * @return id
     */
    static byte[] changeId(String hostString, byte[] id) throws UnknownHostException {
        int r = id[19];
        CRC32C c = new CRC32C();
        byte[] ip = InetAddress.getByName(hostString).getAddress();
        byte[] mask = V4_MASK;
        if (ip.length != 4) {
            // todo ipv6
            throw new IllegalArgumentException("只支持ipv4协议");
        }
        for (int i = 0; i < mask.length; i++) {
            ip[i] &= mask[i];
        }
        ip[0] |= r << 5;
        c.reset();
        c.update(ip, 0, ip.length);
        byte[] crc = i2b((int) c.getValue());
        byte[] result = id.clone();

        System.arraycopy(crc, 0, result, 0, 2);
        result[2] = (byte) (((byte) (crc[2] & 0xf8)) | ((byte) (result[2] & 0b111)));
        result[19] = (byte) r;
        return result;
    }

    private static byte[] i2b(int b) {
        return ByteBuffer.allocate(4).putInt(b).array();
    }
}
