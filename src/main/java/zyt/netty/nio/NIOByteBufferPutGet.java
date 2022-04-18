package zyt.netty.nio;

import java.nio.Buffer;
import java.nio.ByteBuffer;

//当往buffer中存入数据时  取出的时候也要用对应类型的方法进行取出  否则可能会报错
public class NIOByteBufferPutGet {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(64);
        buffer.putInt(100);
        buffer.putLong(1L);
        buffer.putChar('曾');
        buffer.putShort((short) 4);

        buffer.flip();

        //应该按照顺寻进行取出
        System.out.println(buffer.getInt());
        System.out.println(buffer.getLong());
        System.out.println(buffer.getChar());
        System.out.println(buffer.getShort());
    }
}
