package cc.aguesuka.btfind.dht;

/**
 * http://www.bittorrent.org/beps/bep_0005.html#krpc-protocol
 * <p>
 * The KRPC protocol is a simple RPC mechanism consisting of bencoded dictionaries sent over UDP.
 * UdpHandler single query packet is sent out and a single packet is sent in response.
 * There is no retry. There are three message types: query, response, and error.
 * For the DHT protocol, there are four queries: ping, find_node, get_peers, and announce_peer.
 *
 * <p>
 * KRPC 消息的类型是 BencodeMap; key 用 string 表示 value 用 String 表示.
 *
 * @author :yangmingyuxing
 * 2019/8/29 15:06
 */
public class KrpcToken {
    public static final int ID_LENGTH = 20;
    /**
     * Every message has a key "t" with a string value representing a transaction ID.
     * This transaction ID is generated by the querying node and is echoed in the response,
     * so responses may be correlated with multiple queries to the same node.
     * The transaction ID should be encoded as a short string of binary numbers,
     * typically 2 characters are enough as they cover 2^16 outstanding queries.
     */
    public static final String TRANSACTION = "t";
    /**
     * Every message also has a key "y" with a single character value describing the type of message.
     */
    public static final String TYPE = "y";
    /**
     * The value of the "y" key is one of "q" for query
     */
    public static final String TYPE_QUERY = "q";
    /**
     * The value of the "y" key is one of "r" for response
     */
    public static final String TYPE_RESPONSE = "r";
    /**
     * The value of the "y" key is one of "e" for error
     */
    public static final String TYPE_ERROR = "e";
    /**
     * Queries, or KRPC message dictionaries with a "y" value of "q",
     * contain two additional keys; "q" and "a".
     * Key "a" has a dictionary value containing named arguments to the query.
     */
    public static final String ARGUMENTS_MAP = "a";
    /**
     * Key "q" has a string value containing the method name of the query.
     */
    public static final String QUERY = "q";

    /**
     * Responses, or KRPC message dictionaries with a "y" value of "r", contain one additional key "r".
     * The value of "r" is a dictionary containing named return values.
     * Response messages are sent upon successful completion of a query.
     */
    public static final String RESPONSES_MAP = "r";

    public static final String PING = "ping";
    public static final String FIND_NODE = "find_node";
    public static final String GET_PEERS = "get_peers";
    public static final String ANNOUNCE_PEER = "announce_peer";
    public static final String ID = "id";
    public static final String TARGET = "target";
    public static final String NODES = "nodes";
    public static final String INFO_HASH = "info_hash";
    public static final String VALUES = "values";
    public static final String TOKEN = "token";
    public static final String IMPLIED_PORT = "implied_port";
    public static final String PORT = "port";

    /**
     * There is an optional argument called implied_port which value is either 0 or 1.
     * If it is present and non-zero, the port argument should be ignored and the source port of the
     * UDP packet should be used as the peer's port instead.
     * This is useful for peers behind a NAT that may not know their external port, and supporting uTP, they accept incoming
     */
    public static final int NOT_IGNORED_PORT = 0;

}
