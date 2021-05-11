package cc.aguesuka.btfind.connection;

import java.nio.channels.SelectionKey;

/**
 * @author :aguesuka
 * 2019/9/4 13:52
 */
public interface NioHandler {
    /**
     * {@link java.nio.channels.Selector}轮询后,调用该方法处理
     *
     * @param key SelectionKey
     * @throws InterruptedException 只有中断异常能打断循环
     * @throws Exception            其他异常将被打印
     */
    void doHandler(SelectionKey key) throws Exception;
}
