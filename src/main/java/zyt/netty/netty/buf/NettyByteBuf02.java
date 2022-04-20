package zyt.netty.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

public class NettyByteBuf02 {
    public static void main(String[] args) {
        //创建ByteBuf
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello,world!", Charset.forName("utf-8"));

        if (byteBuf.hasArray())//是否初始化过
        {
            byte[] content = byteBuf.array();
            //把content转换为字符串
            System.out.println(new String(content,Charset.forName("utf-8")).trim());

            System.out.println("byteBuf="+byteBuf);

            System.out.println(byteBuf.arrayOffset());//0  数组的偏移量
            System.out.println(byteBuf.readerIndex());//0
            System.out.println(byteBuf.writerIndex());//12
            System.out.println(byteBuf.capacity());//36

            byteBuf.readByte();//会导致readerIndex变换
            byteBuf.getByte(0);//不会导致readerIndex变换

            System.out.println(byteBuf.readableBytes());//可读的容量

            //可以按照区间范围读取
            System.out.println(byteBuf.getCharSequence(0,4,Charset.forName("utf-8")));

        }
    }
}
