package cc.aguesuka.btfind.dht.handler;

import cc.aguesuka.btfind.dht.beans.KrpcMessage;

/**
 * @author :aguesuka
 * 2019/9/11 20:00
 */
public interface IDhtQueryChain extends IBaseDhtChain{
    /**
     * 收到请求时
     *
     * @param query 请求
     */
    default void onQuery(KrpcMessage query){

    }

    /**
     * 收到ping请求
     *
     * @param query 请求消息
     */
    void onPing(KrpcMessage query);

    /**
     * findNode请求
     *
     * @param query 请求消息
     */
    void onFindNodes(KrpcMessage query);

    /**
     * GetPeer请求
     *
     * @param query 请求消息
     */
    void onGetPeer(KrpcMessage query);

    /**
     * AnnouncePeer请求
     *
     * @param query 请求消息
     */
    void onAnnouncePeer(KrpcMessage query);
}
