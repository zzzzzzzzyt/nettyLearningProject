package zyt.netty.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.Random;
import java.util.UUID;


public class MyServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private int count;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] buffer = new byte[msg.readableBytes()];
        msg.readBytes(buffer);
        System.out.println("服务器接收到消息"+new String(buffer, Charset.forName("utf-8")));
        System.out.println("服务器读取次数"+(++this.count));

        ByteBuf buf = Unpooled.copiedBuffer(UUID.randomUUID().toString(), Charset.forName("utf-8"));

        //添加了睡眠的话 就显示的是分开发送了
        // Thread.sleep(new Random().nextInt(4)*1000);
        ctx.writeAndFlush(buf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
