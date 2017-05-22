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
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.concurrent.atomic.AtomicBoolean;

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

    public static void clear() {
        if (instance != null) {
            instance.connected.set(false);
            try {
                instance.future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private ChannelFuture future;
    private AtomicBoolean connected = new AtomicBoolean(false);
    private String host;
    private int port;

    private LoggerTransferClient(String host, int port) {
        this.host = host;
        this.port = port;
        connect(host, port);
    }

    private void connect(String host, int port) {
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
            future.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if (future.isSuccess()) {
                        connected.set(true);
                    } else {
                        connected.set(false);
                    }
                }
            });

            //future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
            eventLoopGroup.shutdownGracefully();
        }
    }

    public void send(byte[] bytes) {
        // 连接失败尝试重连一次
        if (!connected.get())
            connect(host, port);
        if (connected.get()) {
            future.channel().writeAndFlush(bytes);
        } else {
            System.out.println("server not available");
        }
    }
}
