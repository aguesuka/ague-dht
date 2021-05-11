package cc.aguesuka.btfind.dht.handler.chain;

import cc.aguesuka.bencode.util.HexUtil;
import cc.aguesuka.btfind.dao.InfoHashDao;
import cc.aguesuka.btfind.dht.KrpcToken;
import cc.aguesuka.btfind.dht.beans.KrpcMessage;
import cc.aguesuka.btfind.dht.handler.IDhtQueryChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * save hash info when other peer query server
 * you can implement it you self
 *
 * @author :aguesuka
 * 2019/9/12 16:43
 */
@Slf4j
@Component
public class SaveInfoHashChain implements IDhtQueryChain {

    private final InfoHashDao infoHashDao;

    @Autowired
    public SaveInfoHashChain(InfoHashDao infoHashDao) {
        this.infoHashDao = infoHashDao;
    }

    @Override
    public void onPing(KrpcMessage query) {

    }

    @Override
    public void onFindNodes(KrpcMessage query) {

    }

    @Override
    public void onGetPeer(KrpcMessage query) {
        // ignore
    }

    @Override
    public void onAnnouncePeer(KrpcMessage query) {
        try {
            byte[] infoHash = query.getMessage()
                    .getBencodeMap(KrpcToken.ARGUMENTS_MAP)
                    .getByteArray(KrpcToken.INFO_HASH);
            if (infoHash == null || infoHash.length != KrpcToken.ID_LENGTH) {
                return;
            }
            infoHashDao.save(HexUtil.encode(infoHash), query.getAddress());
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
        }
    }
}
