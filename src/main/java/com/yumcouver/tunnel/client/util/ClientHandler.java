package com.yumcouver.tunnel.client.util;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.ConnectException;

public abstract class ClientHandler {
    protected final Logger LOGGER = LogManager.getLogger(this.getClass());
    protected final EventLoopGroup workerGroup = new NioEventLoopGroup();
    protected final ClientHandlerAdapter clientHandlerAdapter;

    public ClientHandler(String host, int port, final ClientHandlerAdapter clientHandlerAdapter) {
        this.clientHandlerAdapter = clientHandlerAdapter;

        final Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(clientHandlerAdapter);
            }
        });
        try {
            bootstrap.connect(host, port).sync();
        } catch (Exception e) {
            if (e instanceof ConnectException) {
                LOGGER.error(e.getLocalizedMessage());
                clientHandlerAdapter.setConnectionRefused();
            } else
                LOGGER.catching(e);
        }
    }

    public synchronized void shutdown() {
        if (clientHandlerAdapter.isConnected()) {
            clientHandlerAdapter.shutdown();
            workerGroup.shutdownGracefully();
        }
    }

    public synchronized void write(byte[] messageBytes) {
        clientHandlerAdapter.write(messageBytes);
    }

    public boolean isConnected() {
        return clientHandlerAdapter.isConnected();
    }

    public boolean isConnectionRefused() {
        return clientHandlerAdapter.isConnectionRefused();
    }
}
