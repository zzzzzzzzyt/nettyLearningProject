package zyt.netty.nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/*
说明
1. MappedByteBuffer 可让文件直接再内存（堆外内存修改），操作系统不需要拷贝一次  属于操作系统级别的操作 性能比较高
 */
public class MappedByteBufferTest {
    public static void main(String[] args) throws Exception{
        RandomAccessFile randomAccessFile = new RandomAccessFile("1.txt","rw");
        //获取对应的通道
        FileChannel channel = randomAccessFile.getChannel();

        /**
         * 解析下这个参数
         * 参数1：FileChannel.MapMode.READ_WRITE 使用的读写模式 应该也是可以设置只读
         * 参数2：可以直接修改的起始为止
         * 参数3：5:是映射到内存的大小（不是索引为止），
         * 可以直接修改的范围按照这个 就是0 1 2 3 4
         * MappedByteBuffer是抽象类  实际类型是DirectByteBuffer
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        mappedByteBuffer.put(0,(byte) 'H');
        mappedByteBuffer.put(2,(byte) 'W');
        System.out.println("修改成功~~");
        randomAccessFile.close();
    }
}
