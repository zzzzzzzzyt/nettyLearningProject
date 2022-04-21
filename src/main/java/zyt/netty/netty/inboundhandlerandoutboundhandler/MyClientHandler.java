package zyt.netty.netty.inboundhandlerandoutboundhandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


public class MyClientHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("接收服务器"+ctx.channel().remoteAddress()+"消息="+msg);
    }


    //重写channelActive 发送消息
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("发送消息");
//        ctx.writeAndFlush(Unpooled.copiedBuffer("aaaaaaaaaaaaaaaa",StandardCharsets.UTF_8));//不会使用到编码器
        ctx.writeAndFlush(12345L);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
