package cc.aguesuka.bencode;

import cc.aguesuka.bencode.util.HexUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author :aguesuka
 * 2019/8/27 14:19
 */

public class BencodeTest {
    private List<byte[]> bencodeData;
    private List<BencodeMap> bencodeMapList;
    private int max;

    @Before
    public void setUp() throws Exception {

        String dataFile = "bencode.txt";
        URL resource = this.getClass().getClassLoader().getResource(dataFile);
        Objects.requireNonNull(resource);
        List<String> lines = Files.readAllLines(Paths.get(resource.toURI()));
        bencodeData = lines.stream().map(HexUtil::decode).collect(Collectors.toList());
        bencodeMapList = bencodeData.stream().map(b -> Bencode.parse(ByteBuffer.wrap(b))).collect(Collectors.toList());
        max = bencodeData.stream().mapToInt(a -> a.length).max().orElse(0);
    }

    @Test
    public void parse() {
        for (byte[] bytes : bencodeData) {
            Assert.assertArrayEquals(bytes, Bencode.parse(ByteBuffer.wrap(bytes)).toBencodeBytes());
        }
    }

    @Test
    public void parseBytes() {
        for (byte[] bytes : bencodeData) {
            BencodeMap parse = Bencode.parse(bytes);
            Assert.assertEquals(parse, Bencode.parse(ByteBuffer.wrap(bytes)));
            Assert.assertArrayEquals(bytes, parse.toBencodeBytes());
        }
    }

    @Test
    public void toBytes() {
        for (BencodeMap bencodeMap : bencodeMapList) {
            Assert.assertEquals(bencodeMap, Bencode.parse(bencodeMap.toBencodeBytes()));
        }
        ArrayList<Object> list = new ArrayList<>();
        BencodeList bList = new BencodeList();
        list.add(1);
        bList.addLong(1);
        list.add(new HashMap<>());
        bList.add(new BencodeMap());
        list.add(new byte[]{1, 2, 3});
        bList.addByteArray(new byte[]{1, 2, 3});
        Assert.assertArrayEquals(Bencode.toBytes(list), Bencode.toBytes(bList));
    }

    @Test
    public void writeToBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(max);
        for (BencodeMap bencodeMap : bencodeMapList) {
            buffer.clear();
            Bencode.writeToBuffer(bencodeMap, buffer).flip();
            Assert.assertEquals(bencodeMap, Bencode.parse(buffer));
        }
    }
}
