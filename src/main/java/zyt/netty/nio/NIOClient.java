package zyt.netty.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {
    public static void main(String[] args) throws Exception{
        //得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //提供服务器端的ip和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1",6666);
        //连接服务器  没连接上也不会阻塞
        if (!socketChannel.connect(inetSocketAddress))
        {
            while (!socketChannel.finishConnect())
            {
                System.out.println("因为连接需要时间，客户端不会阻塞，可以做其他工作...");
            }
        }
        //如果连接成功，就发送数据
        String str = "hello,你会成功的曾~";
        //wraps 就是根据传入的byte数组大小创建相应大小的buffer
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
        socketChannel.write(buffer);
        //停住
        System.in.read();
    }
}
