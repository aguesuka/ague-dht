package cc.aguesuka.btfind.connection;

import cc.aguesuka.btfind.util.record.ActionEnum;
import cc.aguesuka.btfind.util.record.ActionRecord;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author :yangmingyuxing
 * 2019/9/9 12:19
 */
@Component
@Slf4j
public class NioEventLoop implements AutoCloseable {
    @Getter
    @Setter
    private volatile boolean isLoop;
    private CountDownLatch countDownLatch;
    private ActionRecord record;
    @Getter
    private Selector selector;
    public NioEventLoop(ActionRecord record) {
        this.record = record;
    }

    private void stop() {
        log.error("stop loop at:");
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            log.error(element.toString());
        }
        isLoop = false;
    }

    void init() throws IOException {
        this.selector = Selector.open();
    }


    void loop() {
        countDownLatch = new CountDownLatch(1);
        isLoop = true;
        while (isLoop) {
            try {
                select();
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
                stop();
            }
        }
        log.error("loop stop");
        countDownLatch.countDown();
    }


    private void select() {
        record.doRecord(ActionEnum.LOOP_SELECT);
        int selectCount;
        try {
            selectCount = selector.select(100);
        } catch (IOException e) {
            stop();
            log.error(e.getMessage(), e);
            return;
        }
        record.doRecord(ActionEnum.LOOP_KEYS, selectCount);
        if (selectCount > 0) {
            selectedKeys();
        } else {
            allKeys();
        }

    }

    private void selectedKeys() {
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            doHandle(key);
            iterator.remove();
        }
    }

    private void allKeys() {
        Set<SelectionKey> selectionKeys = selector.keys();
        for (SelectionKey key : selectionKeys) {
            doHandle(key);
        }
    }

    private void doHandle(SelectionKey key) {
        Object attachment = key.attachment();
        NioHandler nioHandler = (NioHandler) attachment;
        try {
            nioHandler.doHandler(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void close() throws InterruptedException {
        stop();
        selector.wakeup();
        countDownLatch.await(1000, TimeUnit.SECONDS);
        log.error("loop closed");
    }
}
