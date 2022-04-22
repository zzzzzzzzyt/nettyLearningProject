package zyt.netty.netty.dubborpc.provider;

import zyt.netty.netty.dubborpc.publicinterface.HelloService;

//当有消费方调用接口的时候  就返回这个的 结果
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String msg) {
        System.out.println("收到客户端消息="+msg);
        if (msg!=null)
        {
            return "你好客户端，我已经收到你的消息["+msg+"]";
        }
        else
        {
            return "你好客户端，你发送了空消息";
        }
    }
}
