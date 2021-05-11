package cc.aguesuka.btfind.dht.handler;

import cc.aguesuka.btfind.dht.beans.KrpcMessage;

/**
 * @author :aguesuka
 * 2019/9/11 19:49
 */
public interface IDhtUnknownChain extends IBaseDhtChain{
    /**
     * 收到未知类型的消息
     *
     * @param query 未知类型的消息
     */
    void onUnknownTypeQuery(KrpcMessage query);

    /**
     * 收到错误消息
     *
     * @param error 错误消息
     */
    void onRecvError(KrpcMessage error);

    /**
     * 收到未知类型的消息
     *
     * @param message 未知类型的消息
     */
    void onUnknownType(KrpcMessage message);
}
