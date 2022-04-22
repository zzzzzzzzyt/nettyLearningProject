package zyt.netty.netty.dubborpc.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NettyClient extends ChannelInboundHandlerAdapter {
    //executor用作异步调用
    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static NettyClientHandler clientHandler;

    //编写一个方法使用代理模式,获取一个代理对象  经典 要会 代理模式 jdk 很容易用到  之后手写rpc肯定也会用
    //每次调用获得的都是一个新的代理对象
    public  Object getBean(final Class<?> serviceClass,final String providerName)
    {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{serviceClass},
                (proxy,method,args)-> {
            //{}是客户端每调用一次函数就执行一次
            if (clientHandler==null)
            {
                initClient();
            }
            //providerName协议头  args[0]客户端调用api hello(???)的参数
            clientHandler.setPara(providerName+args[0]);
            return executor.submit(clientHandler).get();
        });
    }

    //初始化客户端
    private static void initClient()
    {
        clientHandler = new NettyClientHandler();
        //创建EventLoopGroup
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(clientHandler);
                    }
                });
        try {
            bootstrap.connect("127.0.0.1",7000).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
