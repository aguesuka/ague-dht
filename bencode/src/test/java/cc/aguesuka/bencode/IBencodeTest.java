package cc.aguesuka.bencode;

import cc.aguesuka.bencode.util.HexUtil;
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
 * @author :aguesuka
 * 2019/9/3 17:46
 */
public class IBencodeTest {
    private ByteBuffer byteBuffer;
    private List<byte[]> bencodeData;
    private List<BencodeMap> bencodeMapList;

    private static byte[] getByteArrayFromByteBuffer(ByteBuffer byteBuffer) {
        byte[] bytesArray = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytesArray);
        return bytesArray;
    }

    @Before
    public void setUp() throws Exception {

        String dataFile = "bencode.txt";
        URL resource = this.getClass().getClassLoader().getResource(dataFile);
        Objects.requireNonNull(resource);
        List<String> lines = Files.readAllLines(Paths.get(resource.toURI()));
        bencodeData = lines.stream().map(HexUtil::decode).collect(Collectors.toList());
        bencodeMapList = bencodeData.stream().map(b -> Bencode.parse(ByteBuffer.wrap(b))).collect(Collectors.toList());
        int max = bencodeData.stream().mapToInt(array -> array.length).max().orElse(0);
        byteBuffer = ByteBuffer.allocate(max);
    }

    @Test
    public void toBencodeBytes() {
        for (int i = 0; i < bencodeData.size(); i++) {
            byteBuffer.clear();
            byte[] bytes = bencodeMapList.get(i).toBencodeBytes();
            byteBuffer.put(bytes);
            Assert.assertArrayEquals(bencodeData.get(i), bytes);
        }
    }

    @Test
    public void toByteBuffer() {
        for (int i = 0; i < bencodeData.size(); i++) {
            ByteBuffer byteBuffer = bencodeMapList.get(i).toByteBuffer();
            byte[] byteArrayFromByteBuffer = getByteArrayFromByteBuffer(byteBuffer);
            Assert.assertArrayEquals(bencodeData.get(i), byteArrayFromByteBuffer);
        }
    }

    @Test
    public void writeToBuffer() {
        for (int i = 0; i < bencodeData.size(); i++) {
            byteBuffer.clear();
            bencodeMapList.get(i).writeToBuffer(byteBuffer).flip();
            byte[] byteArrayFromByteBuffer = getByteArrayFromByteBuffer(byteBuffer);
            Assert.assertArrayEquals(bencodeData.get(i), byteArrayFromByteBuffer);
        }
    }


}