package com.logger.rlc.tcpclient;

import com.logger.common.BytesDecoder;
import com.logger.common.BytesEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * User: rocwu
 * Date: 2017/5/21
 * Time: 下午8:25
 * Desc: 日志发送客户端
 */
public class LoggerTransferClient {

    private static LoggerTransferClient instance;

    public static LoggerTransferClient getInstance(String host, int port) {
        if (instance == null)
            instance = new LoggerTransferClient(host, port);
        return instance;
    }

    private ChannelFuture future;

    private LoggerTransferClient(String host, int port) {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new BytesEncoder());
                            ch.pipeline().addLast(new BytesDecoder());
                            ch.pipeline().addLast(new LoggerClientHandler());
                        }
                    });

            future = bootstrap.connect(host, port).sync();

            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
            eventLoopGroup.shutdownGracefully();
        }
    }

    public void send(byte[] bytes) {
        future.channel().writeAndFlush(bytes);
    }
}
