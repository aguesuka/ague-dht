package cc.aguesuka.btfind.dht.beans;

import cc.aguesuka.bencode.BencodeMap;
import cc.aguesuka.btfind.dht.KrpcToken;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.SocketAddress;

/**
 * @author :yangmingyuxing
 * 2019/9/11 13:10
 */
@Data
@AllArgsConstructor
public class KrpcMessage {
    private BencodeMap message;
    private SocketAddress address;

    public String type() {
        return message.getString(KrpcToken.TYPE);
    }

    public String queryType() {
        return message.getString(KrpcToken.QUERY);
    }

    public byte[] nodes() {
        return message.getBencodeMap(KrpcToken.RESPONSES_MAP).getByteArray(KrpcToken.NODES);
    }
}
