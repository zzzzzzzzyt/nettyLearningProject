package zyt.netty.nio.zerocopy;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

//零拷贝  就是没有了cpu拷贝  大大提高了性能
public class NewIOServer {
    public static void main(String[] args) throws Exception{
        InetSocketAddress address = new InetSocketAddress(7001);
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(address);

        //创建buffer
        ByteBuffer buffer = ByteBuffer.allocate(4096);
        while (true)
        {
            SocketChannel socketChannel = serverSocketChannel.accept();
            int readCount = 0;
            while (readCount!=-1)
            {
                readCount = socketChannel.read(buffer);
                buffer.rewind(); //区别就是buffer.clear() 会将limit置为capacity  一般limit都是capacity 除非使用了flip
            }
        }
    }
}
