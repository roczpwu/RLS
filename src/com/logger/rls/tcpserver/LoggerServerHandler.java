package com.logger.rls.tcpserver;

import com.logger.rls.LoggerWriter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * User: rocwu
 * Date: 2017/5/21
 * Time: 下午8:19
 * Desc: 接收日志处理服务
 */
public class LoggerServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {

        LoggerWriter.getInstance().log((byte[]) msg);

        ctx.writeAndFlush("success".getBytes());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
