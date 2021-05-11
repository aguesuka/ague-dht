package cc.aguesuka.btfind.dht.handler;

import cc.aguesuka.bencode.Bencode;
import cc.aguesuka.bencode.BencodeMap;
import cc.aguesuka.btfind.connection.NioHandler;
import cc.aguesuka.btfind.dht.KrpcToken;
import cc.aguesuka.btfind.dht.beans.KrpcMessage;
import cc.aguesuka.btfind.util.record.ActionEnum;
import cc.aguesuka.btfind.util.record.ActionRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author :aguesuka
 * 2019/9/10 14:22
 */
@Slf4j
@Component
public class DhtHandler implements NioHandler {
    private List<IDhtHandlerChain> handlerChains;
    private Collection<IDhtQueryChain> queryChains;
    private Collection<IDhtUnknownChain> unknownChains;
    private final ApplicationContext applicationContext;
    private final ByteBuffer buffer = ByteBuffer.allocate(1024 * 64);
    private final ActionRecord record;

    @Autowired
    public DhtHandler(ApplicationContext applicationContext,
                      ActionRecord record) {
        this.applicationContext = applicationContext;
        this.record = record;
    }

    /**
     * init chains
     */
    @PostConstruct
    public void init() {
        handlerChains = applicationContext.getBeansOfType(IDhtHandlerChain.class).values()
                .stream()
                .filter(IBaseDhtChain::enable)
                .sorted(Comparator.comparingInt(IDhtHandlerChain::weights).reversed())
                .collect(Collectors.toList());
        queryChains = applicationContext.getBeansOfType(IDhtQueryChain.class).values()
                .stream()
                .filter(IBaseDhtChain::enable)
                .collect(Collectors.toList());
        unknownChains = applicationContext.getBeansOfType(IDhtUnknownChain.class).values()
                .stream()
                .filter(IBaseDhtChain::enable)
                .collect(Collectors.toList());
    }

    private KrpcMessage readMessage(SelectionKey key) {
        try {
            DatagramChannel channel = (DatagramChannel) key.channel();
            buffer.clear();
            SocketAddress address = channel.receive(buffer);
            buffer.flip();
            if (address == null) {
                return null;
            }
            BencodeMap message = Bencode.parse(buffer);
            if (buffer.hasRemaining()) {
                return null;
            }

            KrpcMessage krpcMessage = new KrpcMessage(message, address);
            log.info("recv message `{}`", krpcMessage);
            record.doRecord(ActionEnum.DHT_RECV_LENGTH, buffer.position() / 1024.0);
            return krpcMessage;
        } catch (IOException | RuntimeException e) {
            log.warn(e.getMessage(), e);
            return null;
        }
    }

    private void sendMessage(SelectionKey key, KrpcMessage message) {
        try {
            DatagramChannel channel = (DatagramChannel) key.channel();
            buffer.clear();
            message.getMessage().writeToBuffer(buffer);
            buffer.flip();
            // fixme DatagramChannelImpl#send0 throw exception
            channel.send(buffer, message.getAddress());
            if (buffer.hasRemaining()) {
                throw new DhtHandlerException("send message fail:buff has remaining");
            }
            log.info("send message `{}`", message);
            record.doRecord(ActionEnum.DHT_SEND_SUCCESS);
            record.doRecord(ActionEnum.DHT_SEND_LENGTH, buffer.position() / 1024.0);
        } catch (IOException e) {
            record.doRecord(ActionEnum.DHT_SEND_FAIL);
            log.warn(e.getMessage(), e);
        }
    }

    @Override
    public void doHandler(SelectionKey key) {
        try {
            if (key.isReadable()) {
                KrpcMessage krpcMessage = readMessage(key);
                if (krpcMessage != null) {
                    onReadMessage(krpcMessage);
                }
            } else if (key.isWritable()) {
                for (IDhtHandlerChain chain : handlerChains) {
                    if (chain.isWriteAble()) {
                        KrpcMessage message = chain.getMessage();
                        sendMessage(key, message);
                        break;
                    }
                }
            }
        } finally {
            // update Operation-set
            if (handlerChains.stream().anyMatch(IDhtHandlerChain::isWriteAble)) {
                key.interestOps(SelectionKey.OP_WRITE | SelectionKey.OP_READ);
            } else {
                key.interestOps(SelectionKey.OP_READ);
            }
        }
    }

    private void onReadMessage(KrpcMessage message) {
        String type = message.type();
        if (type == null) {
            unknownChains.forEach(c -> c.onUnknownType(message));
            return;
        }
        switch (type) {
            default:
                unknownChains.forEach(c -> c.onUnknownType(message));
                break;
            case KrpcToken.TYPE_ERROR:
                unknownChains.forEach(c -> c.onRecvError(message));
                break;
            case KrpcToken.TYPE_QUERY:
                onQuery(message);
                queryChains.forEach(c -> c.onQuery(message));
                break;
            case KrpcToken.TYPE_RESPONSE:
                handlerChains.forEach(c -> c.onResponse(message));
                break;
        }
    }

    private void onQuery(KrpcMessage message) {
        String queryType = message.queryType();
        switch (queryType) {
            default:
                unknownChains.forEach(c -> c.onUnknownTypeQuery(message));
                break;
            case KrpcToken.PING:
                queryChains.forEach(c -> c.onPing(message));
                break;
            case KrpcToken.FIND_NODE:
                queryChains.forEach(c -> c.onFindNodes(message));
                break;
            case KrpcToken.GET_PEERS:
                queryChains.forEach(c -> c.onGetPeer(message));
                break;
            case KrpcToken.ANNOUNCE_PEER:
                queryChains.forEach(c -> c.onAnnouncePeer(message));
                break;
        }
    }
}
