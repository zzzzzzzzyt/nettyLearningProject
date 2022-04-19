package zyt.netty.nio.zerocopy;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

//使用transferTo实现零拷贝
public class NewIOClient {
    public static void main(String[] args) throws Exception{
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost",7001));
        String filename = "1.txt";
        //得到一个文件Channel
        FileChannel fileChannel = new FileInputStream(filename).getChannel();

        //准备发送
        long startTime = System.currentTimeMillis();

        //在linux下一个transferTo就可以完成传输
        //在windows下一个只能传输8M 所以需要分段进行发送，根据记录的位置进行发送
        long transToCount = fileChannel.transferTo(0,fileChannel.size(),socketChannel);

        System.out.println("发送的总字节数："+transToCount+",花费的总时长："+(System.currentTimeMillis()-startTime));

        //关闭
        fileChannel.close();
        socketChannel.close();
    }
}
