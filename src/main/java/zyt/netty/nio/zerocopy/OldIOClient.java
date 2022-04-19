package zyt.netty.nio.zerocopy;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.Socket;

//传统的Java io 客户端
public class OldIOClient {
    public static void main(String[] args) throws Exception{
        Socket socket = new Socket("localhost",7001);
        String fileName = "1.txt";
        InputStream inputStream = new FileInputStream(fileName);

        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        byte[] buffer = new byte[4096];
        long readCount;
        long total = 0;
        long startTime = System.currentTimeMillis();
        while ((readCount = inputStream.read(buffer))>=0)
        {
            total+=readCount;
            dataOutputStream.write(buffer,0,(int)readCount);
        }

        System.out.println("发送总字节数："+total+",耗时："+(System.currentTimeMillis()-startTime));
        dataOutputStream.close();
        inputStream.close();
        socket.close();
    }
}
