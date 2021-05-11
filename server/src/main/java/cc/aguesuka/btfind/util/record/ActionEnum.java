package cc.aguesuka.btfind.util.record;

/**
 * @author :aguesuka
 * 2019/9/4 21:22
 */
@SuppressWarnings("unused")
public enum ActionEnum {
    /**
     * action type
     */
    LOOP_SELECT(""),
    LOOP_KEYS(""),
    DHT_READABLE("dht可读次数"),
    DHT_WRITABLE("dht可写次数"),
    DHT_ADD_RESPONSE_QUEUE("添加到回复队列中"),
    DHT_RECV_UNKNOWN_TYPE("收到未知类型消息"),
    DHT_RECV_QUERY("收到请求"),
    DHT_RECV_RESPONSE("收到回复"),
    DHT_RECV_ERROR("收到错误"),
    DHT_SEND_SUCCESS("发送成功"),
    DHT_SEND_FAIL("发送失败"),
    DHT_RECV_UNKNOWN_QUERY("收到未知类型请求"),
    DHT_RECV_PING("收到ping"),
    DHT_RECV_FIND_NODE("收到find_node"),
    DHT_RECV_GET_PEERS("收到get_peers"),
    DHT_RECV_ANNOUNCE_PEER("收到announce_peer"),
    DHT_GET_REPEAT_ADDRESS("收到重复地址"),
    DHT_GET_NEW_ADDRESS("收到新地址"),
    DHT_RECV_LENGTH("收到消息长度kb"),
    DHT_SEND_LENGTH("发送消息长度kb"),
    DHT_EXCEPTION("错误次数"),
    DHT_FILTERED(""),
    JOIN_DHT_INTERVAL("join dht周期"),
    JOIN_DHT_CLEAR("join dht清空路由表"),
    JOIN_DHT_RESTART("join dht重新启动"),
    MD_ADD_TASK(""),
    MD_DHT_CLEAR_GREY("清空灰名单"),
    MD_FINISH_CONNECT(""),
    MD_SEND_HANDSHAKE(""),
    MD_RECV_HANDSHAKE(""),
    MD_SEND_SUPPORT(""),
    MD_RECV_METADATA(""),
    MD_RECV_PACIER(""),
    MD_CHECK_OVER(""),
    MD_SAVE(""),
    MD_RECV_PEER_INFO(""),
    ;
    private final String description;

    ActionEnum(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description.isEmpty() ? name() : description;
    }
}
