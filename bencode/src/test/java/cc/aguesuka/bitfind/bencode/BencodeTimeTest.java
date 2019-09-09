package cc.aguesuka.bitfind.bencode;

import cc.aguesuka.bitfind.bencode.util.HexUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author :yangmingyuxing
 * 2019/9/3 12:49
 */
public class BencodeTimeTest {
    private List<byte[]> bencodeData;
    private List<BencodeMap> bencodeMapList;
    private long startTime;
    private int loopCount = 10000;


    @Before
    public void setUp() throws Exception {

        String dataFile = "bencode.txt";
        URL resource = BencodeTimeTest.class.getClassLoader().getResource(dataFile);
        Objects.requireNonNull(resource);
        List<String> lines = Files.readAllLines(Paths.get(resource.toURI()));
        bencodeData = lines.stream().map(HexUtil::decode).collect(Collectors.toList());
        bencodeMapList = bencodeData.stream().map(b -> Bencode.parse(ByteBuffer.wrap(b))).collect(Collectors.toList());
        for (int i = 0; i < bencodeData.size(); i++) {
            Assert.assertArrayEquals(bencodeData.get(i), bencodeMapList.get(i).toBencodeBytes());
        }
        startTime = System.currentTimeMillis();
    }

    @After
    public void tearDown() {
        long endTime = System.currentTimeMillis();
        double costTime = endTime - startTime;
        System.out.println("costTime(ms) = " + costTime);
        System.out.println("loopCount = " + loopCount);
        System.out.println("bencodeData.size() = " + bencodeData.size());
        double aveDataCost = (costTime / loopCount) / bencodeData.size();
        System.out.println("aveDataCost(ms) = " + aveDataCost);
        System.out.println();
    }


    @Test
    public void parse() {
        System.out.println("parse");
        for (int i = 0; i < loopCount; i++) {
            for (byte[] bencodeDatum : bencodeData) {
                Bencode.parse(ByteBuffer.wrap(bencodeDatum));
            }
        }
    }


    private void runAndPrintTime(Runnable runnable) {
        long l = System.currentTimeMillis();
        runnable.run();
        System.out.println("cost " + (System.currentTimeMillis() - l));
    }

    @Test
    public void timeTest() {
        for (int i = 0; i < 10; i++) {
            System.out.println(i);
            runAndPrintTime(this::parse);
        }
    }

    @Test
    public void toBytes() {
        System.out.println("getBytes");
        for (int i = 0; i < loopCount; i++) {
            for (BencodeMap map : bencodeMapList) {
                Bencode.toBytes(map);
            }
        }
    }
}