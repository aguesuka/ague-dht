package cc.aguesuka.bencode;

import java.nio.ByteBuffer;

/**
 * @author :aguesuka
 * 2019/6/30 16:56
 */
public interface IBencode {
    /**
     * 转为bencode byte数组
     *
     * @return bencode byte数组
     * @throws BencodeException 转换失败
     */
    default byte[] toBencodeBytes() throws BencodeException {
        return Bencode.toBytes(this);
    }

    /**
     * 转为byteBuffer类型
     *
     * @return byteBuffer
     * @throws BencodeException 转换失败
     */
    default ByteBuffer toByteBuffer() throws BencodeException {
        return ByteBuffer.wrap(toBencodeBytes());
    }

    /**
     * 将Bencode的内容写入buffer
     *
     * @param buffer buffer
     * @return 参数 buffer
     * @throws BencodeException 对象中有无法写入的类型
     */
    default ByteBuffer writeToBuffer(ByteBuffer buffer) throws BencodeException {
        return Bencode.writeToBuffer(this, buffer);
    }
}
