package zyt.netty.netty.inboundhandlerandoutboundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

//编写encoder时要注意传入的数据类型和处理的数据类型要一致  不然不会走到encode
public class MyLongToByteEncoder extends MessageToByteEncoder<Long> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Long msg, ByteBuf out) throws Exception {
/*
            这块代码会进行检查判断msg是否是需要进行处理的 不需要的话就退出 不会往下执行
             ByteBuf buf = null;
            try {
                if (acceptOutboundMessage(msg)) {
                    @SuppressWarnings("unchecked")
                    I cast = (I) msg;
                    buf = allocateBuffer(ctx, cast, preferDirect);
                    try {
                        encode(ctx, cast, buf);
                    } finally {
                        ReferenceCountUtil.release(cast);
                    }

                    if (buf.isReadable()) {
                        ctx.write(buf, promise);
                    } else {
                        buf.release();
                        ctx.write(Unpooled.EMPTY_BUFFER, promise);
                    }
                    buf = null;
                } else {
         */
        System.out.println("MyLongToByteEncoder被调用进行编码");
        System.out.println("Msg="+msg);
        out.writeLong(msg);
    }
}
