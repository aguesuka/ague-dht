package cc.aguesuka.btfind.util.record;

import cc.aguesuka.btfind.util.DhtServerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author :yangmingyuxing
 * 2019/9/4 17:37
 */
@Component
@Slf4j
public class ActionRecord {
    private DhtServerConfig config;
    private Map<ActionEnum, Double> sumMap;
    private Map<ActionEnum, Double> lastTimeMap;
    private Map<ActionEnum, Double> nowTimeMap;
    private long lastTime;
    private long startTime;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");

    public ActionRecord(DhtServerConfig config) {
        this.config = config;
        startTime = System.currentTimeMillis();
        lastTime = System.currentTimeMillis();
        nowTimeMap = new EnumMap<>(ActionEnum.class);
        lastTimeMap = new EnumMap<>(ActionEnum.class);
        sumMap = new EnumMap<>(ActionEnum.class);
    }

    public void doRecord(ActionEnum action) {
        doRecord(action, 1);
    }


    public void doRecord(ActionEnum action, double count) {
        putSumMap(action, count);
        checkChangeMap();
        putLastTimeMap(action, count);
    }

    private void putLastTimeMap(ActionEnum action, double count) {
        if (nowTimeMap.containsKey(action)) {
            nowTimeMap.put(action, nowTimeMap.get(action) + count);
        } else {
            nowTimeMap.put(action, count);
        }
    }

    private void putSumMap(ActionEnum action, double count) {
        if (sumMap.containsKey(action)) {
            sumMap.put(action, sumMap.get(action) + count);
        } else {
            sumMap.put(action,  count);
        }
    }

    private void checkChangeMap() {
        int recordTime = config.getRecordTime();
        long cost = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - lastTime);
        if (cost >= recordTime) {
            nowTimeMap = new HashMap<>(lastTimeMap.size());
            lastTime = System.currentTimeMillis();
            log.warn("\n from last time {} record{} \n from start time {} record{}",
                    sdf.format(lastTime), lastTimeMap.toString(),
                    sdf.format(startTime), sumMap.toString());
            lastTimeMap = nowTimeMap;
        }
    }

}
