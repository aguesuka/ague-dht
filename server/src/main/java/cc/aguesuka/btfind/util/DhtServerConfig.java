package cc.aguesuka.btfind.util;

import cc.aguesuka.bencode.util.HexUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * config
 *
 * @author :aguesuka
 * 2019/9/9 12:27
 */
@Getter
@Setter
@Component
@ConfigurationProperties("dht")
public class DhtServerConfig {
    /**
     * 日志记录刷新时间
     */
    private int recordTime = 10;
    /**
     * 本机dht端口
     */
    private int dhtPort = 11111;
    private byte[] selfNodeId;
    /**
     * 本机node id
     */
    private String selfNodeIdHex = "57D438DA296D3E62E0961234BF39A63F895EF126";
    /**
     * 起始节点
     */
    private List<InetSocketAddress> bootstrapNodes;
    /**
     * 如果要支持bep0042,设定该配置项为本机ip
     */
    private String ipForBep0042;
    /**
     * 路由表大小
     */
    private int routingTableSize = 10_000;

    /**
     * 加入dht任务的周期(ms)
     */
    private long joinDhtInterval = 1000;
    /**
     * 加入dht任务每周期的请求数
     */
    private int joinDhtCount = 1000;
    /**
     * join dht 保存的最大地址数量
     */
    private int joinDhtMaxSize = 10000;

    /**
     * dht起始节点,加入dht网络时使用
     *
     * @param bootstrapNodes host:port 格式的地址
     */
    public void setBootstrapNodes(List<String> bootstrapNodes) {
        this.bootstrapNodes = new ArrayList<>();
        for (String rootNode : bootstrapNodes) {
            String[] split = rootNode.split(":");
            String host = split[0];
            int port = Integer.parseInt(split[1]);
            this.bootstrapNodes.add(new InetSocketAddress(host, port));
        }

    }

    @PostConstruct
    public void init() throws UnknownHostException {
        if (selfNodeId == null) {
            selfNodeId = HexUtil.decode(selfNodeIdHex);
        }
        if (ipForBep0042 != null) {
            selfNodeId = Bep0042Impl.changeId(ipForBep0042, selfNodeId);
        }
    }
}
