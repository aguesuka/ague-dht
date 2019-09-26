package cc.aguesuka.btfind.dht.handler.chain;

import cc.aguesuka.btfind.connection.NioEventLoop;
import cc.aguesuka.btfind.dao.InfoHashDao;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author :yangmingyuxing
 * 2019/9/25 18:01
 */
@Component
public class DownloadPeerChain {
    private InfoHashDao infoHashDao;
    private NioEventLoop nioEventLoop;

    public DownloadPeerChain(InfoHashDao infoHashDao, NioEventLoop nioEventLoop) {
        this.infoHashDao = infoHashDao;
        this.nioEventLoop = nioEventLoop;
    }

    @PostConstruct
    public void init() {
        List<String> hexHash = infoHashDao.hexHash();
    }

}
