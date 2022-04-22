package zyt.netty.netty.dubborpc.consumer;

import zyt.netty.netty.dubborpc.netty.NettyClient;
import zyt.netty.netty.dubborpc.publicinterface.HelloService;

public class ClientBootstrap {

    //定义协议头
    public static final String providerName = "HelloService#hello#";

    public static void main(String[] args) {
        //创建一个消费者
        NettyClient customer = new NettyClient();

        //创建代理对象
        HelloService helloService = (HelloService)customer.getBean(HelloService.class, providerName);
        String res = helloService.hello("你好 未来");
        System.out.println("服务端回复信息= "+res);
    }
}
