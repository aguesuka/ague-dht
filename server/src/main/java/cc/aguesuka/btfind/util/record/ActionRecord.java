package cc.aguesuka.btfind.util.record;

import cc.aguesuka.btfind.util.DhtServerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * aggregation log
 *
 * @author :aguesuka
 * 2019/9/4 17:37
 */
@Component
@Slf4j
public class ActionRecord {
    private final DhtServerConfig config;
    private final Map<ActionEnum, Double> sumMap;
    private Map<ActionEnum, Double> lastTimeMap;
    private Map<ActionEnum, Double> nowTimeMap;
    private LocalDateTime lastTime;
    private final LocalDateTime startTime;


    public ActionRecord(DhtServerConfig config) {
        this.config = config;
        startTime = LocalDateTime.now();
        lastTime = LocalDateTime.now();
        nowTimeMap = new EnumMap<>(ActionEnum.class);
        lastTimeMap = new EnumMap<>(ActionEnum.class);
        sumMap = new EnumMap<>(ActionEnum.class);
    }

    public void doRecord(ActionEnum action) {
        doRecord(action, 1);
    }


    public void doRecord(ActionEnum action, double count) {
        putSumMap(action, count);
        onUpdate();
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
            sumMap.put(action, count);
        }
    }

    private void onUpdate() {
        int recordTime = config.getRecordTime();
        long cost = TimeUnit.SECONDS.convert(Duration.between(lastTime, LocalDateTime.now()));
        if (cost >= recordTime) {
            nowTimeMap = new HashMap<>(lastTimeMap.size());
            lastTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            log.warn("\n from last time {} record{} \n from start time {} record{}",
                    lastTime.format(formatter), lastTimeMap.toString(),
                    startTime.format(formatter), sumMap.toString());
            lastTimeMap = nowTimeMap;
        }
    }

}
