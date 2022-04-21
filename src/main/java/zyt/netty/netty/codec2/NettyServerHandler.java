package zyt.netty.netty.codec2;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import zyt.netty.netty.codec.StudentPOJO;

/*
    说明
    1.我们自定义一个Handler 需要继承netty 规定好的某个HandlerAdapter（规范）
    2.这时我们自定义一个Handler ，才能称为一个handler
 */
//两种方法  用Simple那个也可以很方便
//public class NettyServerHandler extends ChannelInboundHandlerAdapter {
public class NettyServerHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {

    /*
         读取数据实际（这里我们可以读取客户端发送的消息）
         ChannelHandlerContext ctx 上下文对象 含有管道pipeline 通道channel 地址
         Object msg 就是客户端发送的数据 默认Object
     */
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        //读取从客户端发送的StudentPOJO.student
//        StudentPOJO.Student student = (StudentPOJO.Student) msg;
//        System.out.println("客户端发送的数据 id="+student.getId()+",name="+student.getName());
//    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx,MyDataInfo.MyMessage msg) throws Exception {
        MyDataInfo.MyMessage.DataType dataType = msg.getDataType();
        if (dataType == MyDataInfo.MyMessage.DataType.studentType) {
            MyDataInfo.Student student = msg.getStudent();
            System.out.println("学生的id="+student.getId()+",学生的名字="+student.getName());
        }
        else if (dataType ==MyDataInfo.MyMessage.DataType.workerType)
        {
            MyDataInfo.Worker worker = msg.getWorker();
            System.out.println("工人的年龄="+worker.getAge()+",工人的名字="+worker.getName());
        }
        else
        {
            System.out.println("传输的类型不正确");
        }
    }

    //数据读取完毕 进行回显数据
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        //writeAndFlush 是write + flush
        //将数据写入到缓存，并刷新
        //一般来讲，要对这个发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~o(=•ェ•=)m",CharsetUtil.UTF_8));
    }

    //处理异常一般是关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //ctx.channel().close(); 两个关闭通道的方法都是可以的
        cause.printStackTrace();
        ctx.close();
    }
}
