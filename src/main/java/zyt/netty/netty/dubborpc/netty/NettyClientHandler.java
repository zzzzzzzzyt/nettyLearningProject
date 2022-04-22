package zyt.netty.netty.dubborpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    private ChannelHandlerContext context;//上下文对象
    private String result;//结果
    private String para;//参数

    //与服务器建立连接后调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
    }

    //收到服务器端消息后调用
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        result = msg.toString();
        notify();//等待唤醒线程
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    //被代理对象调用，发送数据给服务器，-> wait -> 等待被(channelRead)唤醒 -> 返回结果
    @Override
    public synchronized Object call() throws Exception {
        context.writeAndFlush(para);
        wait(); //等待channelRead唤醒
        return result;
    }

    void setPara(String para)
    {
        this.para = para;
    }
}
