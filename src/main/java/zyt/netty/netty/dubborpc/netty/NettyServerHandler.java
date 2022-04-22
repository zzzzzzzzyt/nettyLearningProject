package zyt.netty.netty.dubborpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import zyt.netty.netty.dubborpc.provider.HelloServiceImpl;

//进行处理
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //获取客户端发送消息，并调用服务
        System.out.println("收到消费者端传递消息="+msg);
        //客户端调用服务器api的时候，我们需要定义一个协议  比如 我们要求每次发消息都必须以”HelloService#hello#xxxx“开头
        if(msg.toString().startsWith("HelloService#hello#"))
        {
            String message = msg.toString().substring(msg.toString().lastIndexOf("#") + 1);
            String resultMsg = new HelloServiceImpl().hello(message);
            ctx.writeAndFlush(resultMsg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
