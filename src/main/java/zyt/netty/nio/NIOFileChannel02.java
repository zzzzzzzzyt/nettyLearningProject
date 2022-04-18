package zyt.netty.nio;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 应用实例2-本地文件读数据
 * 实例要求：
 * 1）使用前面学习后的ByteBuffer（缓冲）和FileChannel（通道），将file01.txt中的数据读入到程序，并显示在控制台屏幕
 * 2）假定文件已经存在
 * 3）代码演示
 */
public class NIOFileChannel02 {
    public static void main(String[] args) throws Exception{

        //创建文件的输入流
        File file = new File("d:\\file01.txt");
        FileInputStream fileInputStream = new FileInputStream(file);

        //通过fileInputStreamm 获取对应的FileChannel ->实际类型是这个抽象类的实现
        FileChannel fileChannel = fileInputStream.getChannel();

        //创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate((int)file.length());

        //将通道的数据读入到Buffer
        fileChannel.read(byteBuffer);

        //将byteBuffer的字节数据 转成String
        System.out.println(new String(byteBuffer.array()));
        fileInputStream.close();
    }
}
