package zyt.netty.netty.simple;

import io.netty.util.NettyRuntime;

//当前处理器数目
public class Test {
    public static void main(String[] args) {
        System.out.println(NettyRuntime.availableProcessors());
    }
}
