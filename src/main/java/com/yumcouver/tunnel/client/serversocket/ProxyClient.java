package com.yumcouver.tunnel.client.serversocket;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProxyClient {
    private static final Logger LOGGER = LogManager.getLogger(ProxyClient.class);

    private final EventLoopGroup workerGroup;
    private final int port;

    public void close() {
        workerGroup.shutdownGracefully();
    }

    public ProxyClient(String host, int port, final String key) {
        workerGroup = new NioEventLoopGroup();
        this.port = port;
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ProxyClientHandler(key, ProxyClient.this));
                }
            });
            b.connect(host, port).sync();
        } catch (Exception e) {
            LOGGER.catching(e);
        }
    }

    public int getPort() {
        return port;
    }
}
