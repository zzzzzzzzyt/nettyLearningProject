package zyt.netty.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NettyByteBuf01 {
    public static void main(String[] args) {

        //创建一个ByteBuf
        //说明
        //1.创建对象  该对象包含一个数组arr ，是一个byte[10]
        //2.在netty的buffer中，不需要使用flip进行反转
        // 底层维护了 readerIndex 和 writerIndex
        // 存在已读区域 可读区域 可写区域
        ByteBuf buf = Unpooled.buffer(10);

        for (int i = 0; i < 10; i++) {
            buf.writeByte(i);
        }

        System.out.println("capacity="+buf.capacity());

        for (int i =0;i<buf.capacity();++i)
        {
            buf.readByte();
        }
    }
}
