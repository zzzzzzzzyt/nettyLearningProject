package zyt.netty.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 应用实例3-使用一个Buffer完成文件读取
 * 实例要求
 * 1）使用FileChannel（通道）和方法完成read，write，完成文件的拷贝
 * 2）拷贝一个文本文件1.txt，放在项目下即可
 * 3）代码演示
 */
public class NIOFileChannel03 {
    public static void main(String[] args) throws Exception{

        FileInputStream fileInputStream = new FileInputStream("1.txt");
        FileChannel fileChannel01 = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("2.txt");
        FileChannel fileChannel02 = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        //循环读取
        while (true)
        {
            /*
            这里有个重要的操作不能忘记了 重制所有的标志位
            public final Buffer clear() {
            position = 0;
            limit = capacity;
            mark = -1;
            return this;}
            */
            byteBuffer.clear();  //我看来不重制的话 那就是 position和limit相同了 读取就是一直会是0
            int read = fileChannel01.read(byteBuffer);
            System.out.println("read = "+read);
            byteBuffer.flip();
            if (read==-1)break; //表示读完
            else fileChannel02.write(byteBuffer);
        }
        //关闭流
        fileInputStream.close();
        fileOutputStream.close();
    }
}
