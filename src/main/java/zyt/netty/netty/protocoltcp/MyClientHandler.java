package zyt.netty.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

public class MyClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    private int count;
    //连续发送十个消息 看服务器端的沾包拆包
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 5; ++i) {
            MessageProtocol message = new MessageProtocol();
            String msg = "天气冷，吃火锅"+i;
            message.setLength(msg.getBytes(Charset.forName("utf-8")).length);
            message.setContent(msg.getBytes(Charset.forName("utf-8")));
            ctx.writeAndFlush(message);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        int length = msg.getLength();
        byte[] content = msg.getContent();
        System.out.println("收到服务器回复消息长度"+length);
        System.out.println("收到服务器回复消息："+new String(content,Charset.forName("utf-8")));
        System.out.println("客户端接收的次数"+(++this.count));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
