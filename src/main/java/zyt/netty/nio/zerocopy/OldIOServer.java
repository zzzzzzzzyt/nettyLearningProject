package zyt.netty.nio.zerocopy;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

//传统的Java io 服务器端 进行接受数据
public class OldIOServer {
    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket(7001);
        while (true)
        {
            Socket socket = serverSocket.accept();
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

            byte[] byteArray = new byte[4096];
            while (true)
            {
                int readCount = dataInputStream.read(byteArray,0,byteArray.length);
                if (readCount==-1)break;
            }
        }
    }
}
