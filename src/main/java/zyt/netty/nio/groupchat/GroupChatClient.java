package zyt.netty.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;

/**
 * 客户端功能
 * 1）连接服务器
 * 2）发送消息
 * 3）接收服务器端消息并显示
 */
public class GroupChatClient {
    //定义相关的属性
    private final String HOST = "127.0.0.1"; //服务器的ip
    private final int PORT = 6667; //服务器的端口号
    private Selector selector; //服务器的selector的目的也是区分读和写  在我们发送消息的同时还得接收用户进行发送消息
    private SocketChannel socketChannel;
    private String username;

    //构造器，完成初始化工作
    public GroupChatClient() throws IOException
    {
        selector = Selector.open();
        //连接服务器
        socketChannel = SocketChannel.open(new InetSocketAddress(HOST,PORT));
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //将socketChannel注册到register
        socketChannel.register(selector, SelectionKey.OP_READ);
        //得到username
        username = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(username + " is ok... ");
    }

    //向服务器发送消息
    public void sendInfo(String info)
    {
        info = username + "说:" +info;
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //读取从服务器回复的消息
    public void readInfo()
    {
        try {
            int readChannels = selector.select();
            if (readChannels>0)
            {
                //进行迭代输出
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext())
                {
                    SelectionKey key = keyIterator.next();
                    if (key.isReadable())
                    {
                        //得到相关通道
                        SocketChannel socketChannel = (SocketChannel)key.channel();
                        //设置缓冲区  进行读写
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        int read = socketChannel.read(buffer);
                        String msg = new String(buffer.array(),0,read);
                        System.out.println(msg.trim());
                    }
                    keyIterator.remove();//删除当前的selectionKey防止重复操作
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception{
        //启动我们的客户端
        GroupChatClient chatClient = new GroupChatClient();

        //启动一个线程每隔3秒，读取从服务器发送的数据
        new Thread(()->
        {
            while (true)
            {
                chatClient.readInfo();
                try {
                    Thread.currentThread().sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //客户端发送数据给服务器
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) //会一直阻塞在这 有就会发送
        {
            chatClient.sendInfo(scanner.nextLine());
        }


    }
}
