package zyt.netty.nio;

import java.nio.IntBuffer;

public class BasicBuffer {
    public static void main(String[] args) {
        //举例说明Buffer使用
        //创建一个buffer大小为5 像这个就是可以存放5个int
        IntBuffer intBuffer = IntBuffer.allocate(5);
        //向buffer存放数据
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i*2);
        }
        //如何从buffer读取数据
        //记得要使用flip进行转换 进行读写切换(!!!!) 切换使得buffer中的标志进行变换
        intBuffer.flip();
        //可以设置开始读取的位置
        intBuffer.position(1);
        //设置到最大到哪之前
        intBuffer.limit(3);
        while (intBuffer.hasRemaining())
        {
            System.out.println(intBuffer.get());
        }
    }
}
