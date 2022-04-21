package zyt.netty.netty.codec2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import zyt.netty.netty.codec.StudentPOJO;

import java.util.Random;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    //当通道就绪就会触发该方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //随机的进行发送
        int random = new Random().nextInt(3);
        MyDataInfo.MyMessage myMessage = null;
        if (random==0)
        {
            myMessage = MyDataInfo.MyMessage
                    .newBuilder()
                    .setDataType(MyDataInfo.MyMessage.DataType.studentType)
                    .setStudent(MyDataInfo.Student.newBuilder().setId(2).setName("曾扬添同学").build()).build();
        }
        else
        {
            myMessage = MyDataInfo.MyMessage
                    .newBuilder()
                    .setDataType(MyDataInfo.MyMessage.DataType.workerType)
                    .setWorker(MyDataInfo.Worker.newBuilder().setAge(20).setName("老曾").build()).build();
        }
        ctx.writeAndFlush(myMessage);
    }

    //当通道有读取事件时会触发该方法
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("服务器回复的消息："+buf.toString(CharsetUtil.UTF_8));
        System.out.println("服务器的地址是："+ctx.channel().remoteAddress());
    }

    //异常处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
