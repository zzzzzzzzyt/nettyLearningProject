package zyt.netty.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;


//因为我们是收浏览器的请求  而浏览器的数据是httpObject
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        //判断msg 是不是 httpRequest请求
        if (msg instanceof HttpRequest)
        {
            System.out.println("pipeline hashcode="+ctx.pipeline().hashCode()+",TestHttpServerHandler hash="+this.hashCode());

            System.out.println("msg 类型="+msg.getClass());
            System.out.println("客户端地址"+ctx.channel().remoteAddress());

            //获取到  根据uri过滤特定资源
            HttpRequest httpRequest = (HttpRequest) msg;
            URI uri = new URI(httpRequest.uri());
            if ("/favicon.ico".equals(uri.getPath())){
                System.out.println("请求了 favicon.ico,不做响应");
                return;
            }

            //回复信息给浏览器
            ByteBuf content = Unpooled.copiedBuffer("hello,我是服务器", CharsetUtil.UTF_8);

            //构造一个httpResponse的响应
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK,content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain;charset=utf-8");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());

            //将构建好的response返回
            ctx.writeAndFlush(response);
        }
    }
}
