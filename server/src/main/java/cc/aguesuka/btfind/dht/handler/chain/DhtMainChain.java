package cc.aguesuka.btfind.dht.handler.chain;

import cc.aguesuka.bencode.BencodeByteArray;
import cc.aguesuka.bencode.BencodeMap;
import cc.aguesuka.btfind.dht.KrpcToken;
import cc.aguesuka.btfind.dht.beans.KrpcMessage;
import cc.aguesuka.btfind.dht.handler.IDhtHandlerChain;
import cc.aguesuka.btfind.dht.handler.IDhtQueryChain;
import cc.aguesuka.btfind.util.DhtServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * http://www.bittorrent.org/beps/bep_0005.html
 * response message for other peer query the server
 *
 * @author :aguesuka
 * 2019/9/11 21:56
 */
@Component
public class DhtMainChain implements IDhtQueryChain, IDhtHandlerChain {
    private final Queue<KrpcMessage> messagesQueue = new LinkedList<>();
    private final DhtServerConfig config;
    private final Random random = new SecureRandom();
    private final byte[] token = new byte[2];

    @Autowired
    public DhtMainChain(DhtServerConfig config) {
        this.config = config;
    }

    private void addMessageQueue(KrpcMessage message) {
        messagesQueue.add(message);
    }

    /**
     * Maximum weight
     *
     * @return 10
     */
    @Override
    public int weights() {
        return 10;
    }

    @Override
    public KrpcMessage getMessage() throws NullPointerException {
        return messagesQueue.remove();
    }

    @Override
    public boolean isWriteAble() {
        return !messagesQueue.isEmpty();
    }

    /**
     * 基本回复
     * {y=r, t=aa, r={id=self_id}}
     *
     * @param src 请求消息
     * @return 基本回复
     */
    private KrpcMessage baseResponse(KrpcMessage src) {
        BencodeMap result = new BencodeMap();
        BencodeMap content = new BencodeMap();
        content.putByteArray(KrpcToken.ID, config.getSelfNodeId());
        result.put(KrpcToken.RESPONSES_MAP, content);
        result.put(KrpcToken.TRANSACTION, src.getMessage().get(KrpcToken.TRANSACTION));
        result.putString(KrpcToken.TYPE, KrpcToken.TYPE_RESPONSE);
        return new KrpcMessage(result, src.getAddress());
    }

    @Override
    public void onPing(KrpcMessage query) {
        addMessageQueue(baseResponse(query));
    }

    @Override
    public void onFindNodes(KrpcMessage query) {
        KrpcMessage response = baseResponse(query);
        response.getMessage()
                .getBencodeMap(KrpcToken.RESPONSES_MAP)
                .put(KrpcToken.NODES, BencodeByteArray.empty());
        addMessageQueue(response);
    }

    private byte[] nextToken() {
        random.nextBytes(token);
        return token;
    }

    @Override
    public void onGetPeer(KrpcMessage query) {
        KrpcMessage response = baseResponse(query);
        BencodeMap responseMap = response.getMessage().getBencodeMap(KrpcToken.RESPONSES_MAP);
        responseMap.put(KrpcToken.NODES, BencodeByteArray.empty());
        responseMap.putByteArray(KrpcToken.TOKEN, nextToken());
        addMessageQueue(response);
    }

    @Override
    public void onAnnouncePeer(KrpcMessage query) {
        addMessageQueue(baseResponse(query));
    }
}
