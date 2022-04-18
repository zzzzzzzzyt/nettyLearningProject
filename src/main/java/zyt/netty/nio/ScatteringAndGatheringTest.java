package zyt.netty.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Scattering:将数据写入到buffer时，可以采用buffer数据，依次写入 [分散]
 * Gathering:从buffer读取数据时，可以采用buffer数组，依次读
 */
public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws Exception{
        //使用ServerSocketChannel和SocketChannel 网络   这个写法和bio那个不一样 但是实际上结果都是一样的
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        //绑定端口到socket，并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        //创建buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        //等待客户端（telnet）连接
        SocketChannel socketChannel = serverSocketChannel.accept();
        int messageLength = 8;//假定从客户端接收最多8个

        //循环的读取
        while (true)
        {
            int byteRead = 0;
            while (byteRead<messageLength)
            {
                long l = socketChannel.read(byteBuffers);
                byteRead+=l;//累计读取的字节数
                System.out.println("byteRead="+byteRead);
                //使用流打印 看看当前所有buffer的position和limit   stream.map就是转换成新元素的流 之后进行打印
                Arrays.asList(byteBuffers)
                        .stream()
                        .map(buffer->"position="+buffer.position() +",limit="+buffer.limit())
                        .forEach(System.out::println);
            }
            //将所有的buffer进行flip
            Arrays.asList(byteBuffers)
                    .forEach(buffer->buffer.flip());

            //这下子应该是读取完毕了 这下可以把所有进行打印  将数据显示读回客户端
            long byteWrite = 0;
            while (byteWrite<messageLength)
            {
                long l = socketChannel.write(byteBuffers);
                byteWrite+=l;
            }

            Arrays.asList(byteBuffers)
                    .forEach(buffer->buffer.clear());

            System.out.println("byteRead="+byteRead+",byteWrite="+byteWrite+",messageLength="+messageLength );
        }
    }
}
