package zyt.netty.netty.groupchat;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {
    //定义一个Channel组 管理所有的Channel 为了之后进行转发
    //GlobalEventExecutor.INSTANCE 是一个全局事件执行器 是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //处理器加入监听事件  表示连接建立 就把该通道加入group
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //将该客户加入聊天的消息推送给其他在线用户
        /*
            该方法会将channelGroup中的所有channel遍历 并发送消息
         */
        channelGroup.writeAndFlush(sdf.format(new Date())+"[客户端]"+channel.remoteAddress()+"加入聊天\n");
        channelGroup.add(channel);
    }

    //处理器移除监听事件  会自动移除不需要 进行手动移除
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush(sdf.format(new Date())+"[客户端]"+channel.remoteAddress()+"离开聊天\n");
        System.out.println("channelGroup Size="+channelGroup.size());
    }

    //处理器活跃监听
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(sdf.format(new Date())+"[客户端]"+channel.remoteAddress()+"上线了😊\n");
    }

    //处理器不活跃监听
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(sdf.format(new Date())+"[客户端]"+channel.remoteAddress()+"下线了😊\n");
    }

    //读事件 进行转发
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.forEach(ch->{
            if (ch!=channel)
            {
                ch.writeAndFlush(sdf.format(new Date())+"[客户端]"+ch.remoteAddress()+"发送了消息"+msg+"\n");
            }
            else
            {
                ch.writeAndFlush(sdf.format(new Date())+"[自己]"+"发送了消息"+msg+"\n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
