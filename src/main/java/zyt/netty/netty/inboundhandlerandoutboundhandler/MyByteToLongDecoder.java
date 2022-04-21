package zyt.netty.netty.inboundhandlerandoutboundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyByteToLongDecoder extends ByteToMessageDecoder {
    /**
     *
     * decode 会根据接收的数据，被调用多次，直到确定没有新的元素被添加到list，或者是ByteBuf没有更多的可读字节为止
     * 如果 list out不为空 就会将list的内容传递给下一个channelInBoundHandler处理 该处理器的方法也会被调用多次
     *
     * @param ctx 上下文对象
     * @param in  入栈的byteBuf
     * @param out List集合，将解码后的数据传给下一个handler处理
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        System.out.println("调用解码器");
        //因为Long有8个字节  这个if只要in里超过8就会进行循环的读取  没超过就不能进行解码  因为传输的都是字节码
        if (in.readableBytes()>=8)
        {
            out.add(in.readLong());
        }
    }
}
