package zyt.netty.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * 案例要求：
 * 1）编写一个NIO入门案例，实现服务器端和客户端之间的数据简单通讯（非阻塞）
 * 2）目的：理解NIO非阻塞网络编程机制
 * 3）代码演示
 */
public class NIOServer {
    public static void main(String[] args) throws Exception{
        //创建ServerSocketChannel ->ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //得到一个Selector对象
        Selector selector = Selector.open();

        //绑定一个端口6666，在服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        //将serverSocketChannel注册到selector中去  关心的事件是SelectionKey.OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //循环等待客户端连接
        while (true)
        {
            //这里我们等待1秒
            if (selector.select(1000)==0)//没有事件发生
            {
                System.out.println("服务器等待了1秒，无连接");
                continue;
            }
            //否则就是有事件发生 开始进行遍历 检查事件的发生情况
            //那么就获取selectionKey集合
            //通过selectionKeys 反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            //遍历Set<SelectionKey>，使用迭代器遍历
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while (keyIterator.hasNext())
            {
                //获取到SelectionKey
                SelectionKey key = keyIterator.next();
                //根据key 对应的通道发生的事件做处理
                if (key.isAcceptable()) //如果是OP_ACCEPT 有新的客户端连接
                {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功 生成了一个socketChannel"+socketChannel.hashCode());
                    //将socketChannel设置为非阻塞
                    socketChannel.configureBlocking(false);
                    //将socketChannel 注册到selector中去，关注事件为OP_READ 同时给socketChannel关联一个Buffer
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));

                }
                if (key.isReadable())//发生 OP_READ
                {
                    //通过key 反向获取到对应的channel
                    SocketChannel channel = (SocketChannel)key.channel();
                    //通过key 反向获取到buffer
                    ByteBuffer buffer = (ByteBuffer)key.attachment();
                    //意思是把当前通道里的数据读入到buffer中去
                    int read = channel.read(buffer);
                    System.out.println("from 客户端"+new String(buffer.array(),0,read));
                }
                //手动从集合中移除当前的selectionKey，防止重复操作
                keyIterator.remove();
            }
        }
    }
}
