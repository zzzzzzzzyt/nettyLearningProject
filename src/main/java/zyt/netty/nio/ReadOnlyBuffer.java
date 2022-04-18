package zyt.netty.nio;

import java.nio.ByteBuffer;

public class ReadOnlyBuffer {
    public static void main(String[] args) {
        //创建一个buffer
        ByteBuffer buffer = ByteBuffer.allocate(128);

        for (int i = 0; i < 64; i++) {
            buffer.put((byte) i);
        }

        //读取
        buffer.flip();

        //转换为只读
        ByteBuffer asReadOnlyBuffer = buffer.asReadOnlyBuffer();

        //读取
        while (asReadOnlyBuffer.hasRemaining())
        {
            System.out.println(asReadOnlyBuffer.get());
        }

        //ReadOnlyBufferException 只读的buffer里再放数据就会出现这样的异常
        asReadOnlyBuffer.put((byte) 100);
    }
}
