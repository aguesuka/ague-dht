package cc.aguesuka.btfind.connection;

import cc.aguesuka.btfind.dht.handler.DhtHandler;
import cc.aguesuka.btfind.util.DhtServerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;

/**
 * bootstrap
 *
 * @author :aguesuka
 * 2019/9/9 13:39
 */
@Slf4j
@Component
public class Bootstrap {
    private final NioEventLoop nioEventLoop;
    private final DhtServerConfig config;
    private final DhtHandler dhtHandler;

    @Autowired
    public Bootstrap(NioEventLoop nioEventLoop,
                     DhtServerConfig config,
                     DhtHandler dhtHandler) {
        this.nioEventLoop = nioEventLoop;
        this.config = config;
        this.dhtHandler = dhtHandler;
    }

    /**
     * start dht network
     *
     * @throws IOException If an I/O error occurs
     */
    public void start() throws IOException {
        nioEventLoop.init();
        DatagramChannel udpChannel = DatagramChannel.open();
        udpChannel.bind(new InetSocketAddress(config.getDhtPort()))
                .configureBlocking(false);
        udpChannel.register(nioEventLoop.getSelector(), SelectionKey.OP_WRITE | SelectionKey.OP_READ, dhtHandler);
        log.error("sever start");
        nioEventLoop.loop();
        log.error("sever stop");
    }


    @PreDestroy
    public void end() throws IOException, InterruptedException {
        nioEventLoop.close();
        nioEventLoop.getSelector().close();
    }
}
