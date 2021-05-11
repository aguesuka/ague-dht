package cc.aguesuka.btfind.dht.handler.chain;

import cc.aguesuka.btfind.dht.beans.KrpcMessage;
import cc.aguesuka.btfind.dht.handler.IDhtHandlerChain;
import cc.aguesuka.btfind.dht.handler.IDhtQueryChain;
import cc.aguesuka.btfind.dht.handler.IDhtUnknownChain;
import cc.aguesuka.btfind.util.record.ActionRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cc.aguesuka.btfind.util.record.ActionEnum.*;

/**
 * @author :aguesuka
 * 2019/9/12 13:27
 */
@Component
public class DhtRecordChain implements IDhtHandlerChain, IDhtQueryChain, IDhtUnknownChain {
    private final ActionRecord record;

    @Autowired
    public DhtRecordChain(ActionRecord record) {
        this.record = record;
    }

    @Override
    public void onResponse(KrpcMessage response) {
        record.doRecord(DHT_RECV_RESPONSE);
    }


    @Override
    public void onPing(KrpcMessage query) {
        record.doRecord(DHT_RECV_PING);
    }

    @Override
    public void onFindNodes(KrpcMessage query) {
        record.doRecord(DHT_RECV_FIND_NODE);

    }

    @Override
    public void onGetPeer(KrpcMessage query) {
        record.doRecord(DHT_RECV_GET_PEERS);

    }

    @Override
    public void onAnnouncePeer(KrpcMessage query) {
        record.doRecord(DHT_RECV_ANNOUNCE_PEER);
    }

    @Override
    public void onUnknownTypeQuery(KrpcMessage query) {
        record.doRecord(DHT_RECV_UNKNOWN_QUERY);
    }

    @Override
    public void onRecvError(KrpcMessage error) {
        record.doRecord(DHT_RECV_ERROR);

    }

    @Override
    public void onQuery(KrpcMessage query) {
        record.doRecord(DHT_RECV_QUERY);
    }

    @Override
    public void onUnknownType(KrpcMessage message) {
        record.doRecord(DHT_RECV_UNKNOWN_TYPE);
    }
}
