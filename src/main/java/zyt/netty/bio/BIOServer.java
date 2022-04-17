package zyt.netty.bio;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 实例说明：
 * 1）使用BIO模型编写一个服务器端，监听6666端口，当有客户端连接时，就启动一个线程与之通讯。
 * 2）要求使用线程池机制改善，可以连接多个客户端。
 * 3）服务器端可以接受客户端发送的数据（telnet方式即可）  使用cmd 输入telnet 127.0.0.1 6666 即可连接上 然后ctrl+]就可以进入命令行  发送send+需要的字符
 */
public class BIOServer {
    public static void main(String[] args) throws Exception{

        //思路
        //1.创建一个线程池
        //2.如果有客户端连接，就创建一个线程，与之通讯（单独写一个方法）
        ExecutorService threadPool = Executors.newCachedThreadPool();
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器启动了");
        while (true)
        {
            //进行监听 等待客户端连接
            System.out.println("等待连接");
            final Socket socket = serverSocket.accept();
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    //进行消息通讯
                    System.out.println("连接到一个客户端"+Thread.currentThread().getName());
                    handler(socket);
                }
            });
        }
    }

    //编写一个handler方法，和客户端通讯   顺便练习了 io的读写
    private static void handler(Socket socket) {
        try {
            byte[] bytes = new byte[1024];
            InputStream inputStream = socket.getInputStream();
            while (true)
            {
                //这里很明显的表示 就算没有数据输入  也会阻塞在这里
                System.out.println("等待读取数据......");
                int read = inputStream.read(bytes);
                if (read!=-1)
                {
                    System.out.println(Thread.currentThread().getName()+new String(bytes,0,read));
                }
                else
                {
                    break;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try {
                socket.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
