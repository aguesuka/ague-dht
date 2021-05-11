package cc.aguesuka.btfind.util;

import cc.aguesuka.bencode.util.HexUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * the command util parser ip byte array to string
 *
 * @author :aguesuka
 * 2019/9/11 18:07
 */
public class GetIp {
    public static void main(String[] args) throws UnknownHostException {
        String ipHex = "DF9B45D2";
        if (args.length > 0) {
            ipHex = args[0];
        }
        System.out.println(InetAddress.getByAddress(HexUtil.decode(ipHex)));

    }
}
