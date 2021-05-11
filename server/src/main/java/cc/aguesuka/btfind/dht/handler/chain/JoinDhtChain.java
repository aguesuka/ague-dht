package cc.aguesuka.btfind.dht.handler.chain;

import cc.aguesuka.bencode.BencodeMap;
import cc.aguesuka.btfind.dht.KrpcToken;
import cc.aguesuka.btfind.dht.beans.KrpcMessage;
import cc.aguesuka.btfind.dht.handler.IDhtHandlerChain;
import cc.aguesuka.btfind.util.DhtServerConfig;
import cc.aguesuka.btfind.util.record.ActionEnum;
import cc.aguesuka.btfind.util.record.ActionRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.*;

/**
 * @author :aguesuka
 * 2019/9/12 13:37
 */
@Slf4j
@Component
public class JoinDhtChain implements IDhtHandlerChain {

    private final Queue<SocketAddress> waitSend = new LinkedList<>();
    private final Set<SocketAddress> blackList = new HashSet<>();
    private final Set<SocketAddress> allNode = new HashSet<>();
    private final Queue<SocketAddress> newNode = new LinkedList<>();
    private final Queue<SocketAddress> goodNode = new LinkedList<>();

    private final ActionRecord record;
    private final Random random = new SecureRandom();
    private final byte[] target = new byte[20];
    private final DhtServerConfig config;
    private long lastTime;

    @Autowired
    public JoinDhtChain(ActionRecord record, DhtServerConfig config) {
        this.record = record;
        this.config = config;
    }

    private <T, E extends T> void addQueue(Queue<T> queue, E element) {
        if (queue.size() >= config.getJoinDhtMaxSize()) {
            queue.remove();
        }
        queue.add(element);
    }

    private <T, E extends T> void moveUntil(Collection<T> target, Queue<E> src, int count) {
        while (target.size() < count && !src.isEmpty()) {
            target.add(src.remove());
        }
    }

    private void updateQueue() {
        int count = config.getJoinDhtCount();
        moveUntil(waitSend, newNode, count);
        if (waitSend.size() > 0 && allNode.size() <= config.getJoinDhtMaxSize()) {
            return;
        }
        allNode.clear();
        moveUntil(waitSend, goodNode, count);
        if (waitSend.size() > 0) {
            record.doRecord(ActionEnum.JOIN_DHT_CLEAR);
            return;
        }
        record.doRecord(ActionEnum.JOIN_DHT_RESTART);
        waitSend.addAll(config.getBootstrapNodes());
    }

    private void update() {
        if (System.currentTimeMillis() - lastTime > config.getJoinDhtInterval()) {
            updateQueue();
            lastTime = System.currentTimeMillis();
            record.doRecord(ActionEnum.JOIN_DHT_INTERVAL);
        }
    }

    private BencodeMap findNodeQuery() {
        BencodeMap result = new BencodeMap();
        byte[] transaction = new byte[4];
        random.nextBytes(transaction);
        result.putByteArray(KrpcToken.TRANSACTION, transaction);
        result.putString(KrpcToken.TYPE, KrpcToken.QUERY);
        result.putString(KrpcToken.QUERY, KrpcToken.FIND_NODE);
        BencodeMap argumentsMap = new BencodeMap();
        argumentsMap.putByteArray(KrpcToken.ID, config.getSelfNodeId());
        random.nextBytes(target);
        argumentsMap.putByteArray(KrpcToken.TARGET, target);
        result.put(KrpcToken.ARGUMENTS_MAP, argumentsMap);
        return result;
    }

    @Override
    public int weights() {
        return 0;
    }

    @Override
    public void onResponse(KrpcMessage response) {
        BencodeMap responseMap = response.getMessage().getBencodeMap(KrpcToken.RESPONSES_MAP);
        byte[] senderId = responseMap.getByteArray(KrpcToken.ID);
        if (Arrays.equals(senderId, config.getSelfNodeId())) {
            blackList.add(response.getAddress());
            return;
        }
        addQueue(goodNode, response.getAddress());

        byte[] nodes = response.nodes();
        byte[] id = new byte[KrpcToken.ID_LENGTH];
        byte[] ip = new byte[4];
        if (nodes == null) {
            blackList.add(response.getAddress());
            return;
        }
        ByteBuffer nodesBuffer = ByteBuffer.wrap(nodes);
        while (nodesBuffer.hasRemaining()) {
            nodesBuffer.get(id);
            nodesBuffer.get(ip);
            int port = nodesBuffer.getShort() & 0xffff;
            try {
                InetSocketAddress address = new InetSocketAddress(InetAddress.getByAddress(ip), port);
                if (allNode.contains(address) || blackList.contains(address)) {
                    record.doRecord(ActionEnum.DHT_GET_REPEAT_ADDRESS);
                    return;
                }
                record.doRecord(ActionEnum.DHT_GET_NEW_ADDRESS);

                allNode.add(address);
                addQueue(newNode, address);
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public KrpcMessage getMessage() {
        return new KrpcMessage(findNodeQuery(), waitSend.remove());
    }

    @Override
    public boolean isWriteAble() {
        update();
        return !waitSend.isEmpty();
    }
}
