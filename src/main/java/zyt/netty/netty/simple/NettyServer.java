package zyt.netty.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args) throws Exception{
        //创建bossGroup workerGroup
        //前者只处理连接请求  后者处理真正的业务请求
        //两个都是无限循环
        // bossGroup、workerGroup 含有子线程(NIOEventLoop)的个数 默认是cpu*2
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //创建服务器端的启动对象 配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();

            //使用链式编程设置
            bootstrap.group(bossGroup,workerGroup) //设置两个线程组
                    .channel(NioServerSocketChannel.class) //使用NioServerSocketChannel 作为服务器通道实现
                    .option(ChannelOption.SO_BACKLOG,128) //设置线程队列得到的连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE,true) //设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() { //创建一个通道处理对象（匿名对象）
                        //给pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //每个用户来都会注册一个channel
                            System.out.println("客户socketChannel hashcode="+ch.hashCode());
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    });//给我们的workerGroup 的eventLoop 对应的管道设置处理器

            System.out.println("服务器 is ready");

            //绑定一个端口并且同步，生成一个channelFuture对象
            //启动服务器绑定端口
            ChannelFuture cf = bootstrap.bind(6668).sync();

            //给cf注册监听器
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println("监听端口 6668成功");
                    }
                    else
                    {
                        System.out.println("监听端口 6668失败");
                    }
                }
            });

            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } finally {
            //优雅的关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
