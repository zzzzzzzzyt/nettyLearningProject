package zyt.netty.netty.dubborpc.provider;

import zyt.netty.netty.dubborpc.netty.NettyServer;

//启动一个服务提供者
public class ServerBootstrap {
    public static void main(String[] args) {
        NettyServer.startServer("127.0.0.1",7000);
    }
}
