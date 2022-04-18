package zyt.netty.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 应用实例1-本地文件写数据
 * 实例要求：
 * 1）使用前面学习后的ByteBuffer（缓冲）和FileChannel（通道），将“hello，曾扬添”写入到file01.txt中
 * 2）文件不存在就创建
 * 3）代码演示
 */
public class NIOFileChannel01 {
    public static void main(String[] args) throws Exception{
        String str = "hello，曾扬添";
        //创建一个输出流->channel 本质上还是通过输出流进行读写的 只不过用channel进行了包装
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\file01.txt");

        //通过输出流获取对应的Channel
        //这个FileChannel的真实类型是fileChannelImpl    是outputStream中包裹了channel
        FileChannel fileChannel = fileOutputStream.getChannel();

        //创建一个缓存区 然后把缓存区的数据写进channel  构建缓存区的函数要用 XxBuffer.allocate(想分配的容量)
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //将数据写入
        byteBuffer.put(str.getBytes());

        //对byteBuffer进行反转
        byteBuffer.flip();

        //将buffer中的数据写入channel
        fileChannel.write(byteBuffer);
        fileChannel.close();
    }
}
