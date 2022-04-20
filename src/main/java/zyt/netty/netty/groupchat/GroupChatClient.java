package zyt.netty.netty.groupchat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

public class GroupChatClient {
    private final String hostname;
    private final int port;

    public GroupChatClient(String hostname,int port)
    {
        this.hostname = hostname;
        this.port = port;
    }

    public void run() throws Exception
    {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("decoder",new StringDecoder());
                            pipeline.addLast("coder",new StringEncoder());
                            pipeline.addLast(new GroupChatClientHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(hostname, port).sync();
            Channel channel = channelFuture.channel();
            System.out.println("---------"+channel.localAddress()+"-----------");
            Scanner sc = new Scanner(System.in);
            while (sc.hasNextLine())
            {
                String msg = sc.nextLine()+"\r\n";
                //通过channel发送到服务器端
                channel.writeAndFlush(msg);
            }
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception{
        new GroupChatClient("127.0.0.1",7000).run();
    }
}
