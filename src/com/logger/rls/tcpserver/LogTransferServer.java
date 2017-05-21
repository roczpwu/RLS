package com.logger.rls.tcpserver;

import com.logger.common.BytesDecoder;
import com.logger.common.BytesEncoder;
import com.logger.rls.ServerConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * User: rocwu
 * Date: 2017/5/21
 * Time: 下午8:05
 * Desc: 日志传输服务端
 */
public class LogTransferServer {

    private final int port;

    public LogTransferServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new BytesEncoder());
                            ch.pipeline().addLast(new BytesDecoder());
                            ch.pipeline().addLast(new LoggerServerHandler());
                        }
                    });

            // Bind and start to accept incoming connections.
            b.bind(port).sync().channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = ServerConfig.getServerConfig().getPort();
        LogTransferServer server = new LogTransferServer(port);
        server.run();
    }
}
