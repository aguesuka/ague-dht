package cc.aguesuka.btfind.dht.handler;

import cc.aguesuka.btfind.dht.beans.KrpcMessage;

import java.util.NoSuchElementException;

/**
 * @author :aguesuka
 * 2019/9/11 19:34
 */
public interface IDhtHandlerChain extends IBaseDhtChain {

    /**
     * 权重,权重高的优先处理
     *
     * @return 权重
     */
    default int weights() {
        return 0;
    }

    /**
     * 收到回复时
     *
     * @param response 回复
     */
    default void onResponse(KrpcMessage response) {
    }

    /**
     * 可写时
     *
     * @return 消息
     * @throws NullPointerException          如果没有可写的消息则抛出异常
     * @throws NoSuchElementException        如果没有可写的消息则抛出异常
     * @throws UnsupportedOperationException 不支持的操作
     */
    default KrpcMessage getMessage() {
        throw new UnsupportedOperationException();
    }

    /**
     * 是否有消息可以发送
     *
     * @return 是否有消息可以发送
     */
    default boolean isWriteAble() {
        return false;
    }

}
