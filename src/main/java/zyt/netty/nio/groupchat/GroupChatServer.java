package zyt.netty.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * 服务器端的功能：
 * 1）服务器启动并监听6667
 * 2）服务器接受客户端信息，并实现转发【处理上线和下线】
 */
public class GroupChatServer {
    //定义属性
    private Selector selector;
    private ServerSocketChannel listenerChannel;
    private static final int PORT = 6667;

    //构造器进行初始化
    public GroupChatServer()
    {
        try {
            //创建selector
            selector = Selector.open();
            //创建ServerSocketChannel
            listenerChannel = ServerSocketChannel.open();
            //绑定监听端口
            listenerChannel.socket().bind(new InetSocketAddress(PORT));
            //设置非阻塞
            listenerChannel.configureBlocking(false);
            //将该listenerChannel注册到selector
            listenerChannel.register(selector, SelectionKey.OP_ACCEPT);

        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    //监听
    public void listen()
    {
        try {
            //进行循环的处理
            while (true) {
                int count = selector.select();

                if (count>0)
                {
                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    while (keyIterator.hasNext())
                    {
                        SelectionKey key = keyIterator.next();
                        if (key.isAcceptable()) //处理accept事件
                        {
                            SocketChannel sc = listenerChannel.accept();
                            sc.configureBlocking(false); //千万记得设置非阻塞
                            //将该sc注册到selector中去
                            sc.register(selector,SelectionKey.OP_READ);
                            //提示
                            System.out.println(sc.getRemoteAddress()+" 上线了 ");
                        }
                        if (key.isReadable()) //处理读事件
                        {
                            readData(key);
                        }
                        //千万注意 要把当前的key删除 防止重复作用
                        keyIterator.remove();
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                selector.close();
                listenerChannel.close();
            }catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


    //读取客户端消息
    private void readData(SelectionKey key)
    {
        SocketChannel channel = null;
        try {
            //得到channel
            channel = (SocketChannel) key.channel();
            //创建buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            //根据count的值做处理
            int count = channel.read(buffer);
            if (count>0)
            {
                //将缓冲区的数据转换成字符串
                String msg = new String(buffer.array(),0,count);
                System.out.println("from 客户端"+channel.getRemoteAddress()+":"+msg);
                //向其他客户端转发消息 专门写一个方法 我的想法是遍历所有的key 然后除了本身的key
                sendInfoToOtherClients(msg,channel);
            }
        }catch (IOException e)
        {
            try {
                //因为断开连接了才会出现IO异常
                System.out.println(channel.getRemoteAddress()+"离线了...");
                //key取消注册
                key.cancel();
                //通道进行关闭
                channel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    //转发消息给其他客户(通道)
    private void sendInfoToOtherClients(String msg,SocketChannel self) throws IOException{
        System.out.println("服务器转发消息中...");
        Set<SelectionKey> keys = selector.keys();
        Iterator<SelectionKey> keyIterator = keys.iterator();
        while (keyIterator.hasNext())
        {
            SelectionKey key = keyIterator.next();
            Channel targetChannel = key.channel();
            if (targetChannel instanceof SocketChannel&&targetChannel!=self)
            {
                SocketChannel dest = (SocketChannel) targetChannel;
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                dest.write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        //创建一个服务器对象
        GroupChatServer chatServer = new GroupChatServer();
        chatServer.listen();
    }
}
