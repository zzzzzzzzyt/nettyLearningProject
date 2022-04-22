package zyt.netty.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.UUID;


public class MyServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    private int count;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        int length = msg.getLength();
        byte[] content = msg.getContent();
        System.out.println("服务器接收到消息长度"+length);
        System.out.println("服务器接收到消息"+new String(content, Charset.forName("utf-8")));
        System.out.println("服务器读取次数"+(++this.count));

        //服务器收到 进行回送消息
        String responseMsg = UUID.randomUUID().toString();
        byte[] responseBytes = responseMsg.getBytes(Charset.forName("utf-8"));
        int bytesLength = responseBytes.length;
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLength(bytesLength);
        messageProtocol.setContent(responseBytes);
        ctx.writeAndFlush(messageProtocol);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
