package zyt.netty.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * 应用实例4-拷贝文件transferFrom方法
 * 实例要求：
 * 1）使用FileChannel（通道）和方法 transferFrom，完成文件的拷贝
 * 2）拷贝一张图片
 * 3）代码演示
 */
public class NIOFileChannel04 {
    public static void main(String[] args) throws Exception{
        //创建对应的流
        FileInputStream fileInputStream = new FileInputStream("d:\\01.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\02.jpg");

        //创建对应的通道
        FileChannel sourceChannel = fileInputStream.getChannel();
        FileChannel destinationChannel = fileOutputStream.getChannel();

        //使用transferFrom进行拷贝
        destinationChannel.transferFrom(sourceChannel,0,sourceChannel.size());

        //关闭对应的通道和流
        sourceChannel.close();
        destinationChannel.close();
        fileInputStream.close();
        fileOutputStream.close();
    }
}
