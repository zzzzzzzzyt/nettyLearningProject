package zyt.netty.netty.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
    public static void main(String[] args) throws Exception{
        //client也有group
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            //创建客户端的启动对象 并进行配置
            Bootstrap bootstrap = new Bootstrap();

            //使用链式编程设置
            bootstrap.group(group) //设置线程组
                    .channel(NioSocketChannel.class) //设置客户端通道的实现类
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyClientHandler());
                        }
                    });

            System.out.println("客户端 is ok");

            //进行端口的连接
            ChannelFuture cf = bootstrap.connect("127.0.0.1", 6668).sync();

            //对关闭通道进行监听
            cf.channel().closeFuture().sync();

        } finally {
            group.shutdownGracefully();
        }
    }
}
