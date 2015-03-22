package com.yumcouver.tunnel.client.serversocket;

import com.google.protobuf.ByteString;
import com.yumcouver.tunnel.client.protobuf.TunnelProto;
import com.yumcouver.tunnel.client.util.ConfigReader;
import com.yumcouver.tunnel.client.websocket.TCPTunnelClientEndpoint;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class ListeningServer implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger(ListeningServer.class);
    private static ListeningServer ourInstance = new ListeningServer();
    private static final boolean OPEN = true;
    private static final boolean CLOSE = false;

    public static ListeningServer getInstance() {
        return ourInstance;
    }

    private final ServerBootstrap serverBootstrap;
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;
    private final Thread thread = new Thread(this);
    private String destinationId;
    private String destinationIp;
    private int destinationPort;
    private final int listeningPort;
    private volatile boolean status = CLOSE;
    private final TCPTunnelClientEndpoint tcpTunnelClientEndpoint =
            TCPTunnelClientEndpoint.getInstance();

    private ListeningServer() {
        this(ConfigReader.LISTENING_PORT);
    }

    private ListeningServer(int listeningPort) {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new RequestHandler());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        this.listeningPort = listeningPort;
    }

    public void send(int sourcePort, byte[] message) throws IOException {
        if (status == OPEN) {
            TunnelProto.TunnelCommand tunnelCommand = TunnelProto.TunnelCommand.newBuilder()
                    .setMethod(TunnelProto.TunnelCommand.Method.SEND)
                    .setSourceType(TunnelProto.TunnelCommand.EndType.CLIENT)
                    .setSourceId(tcpTunnelClientEndpoint.getSessionId())
                    .setDestinationType(TunnelProto.TunnelCommand.EndType.CLIENT)
                    .setDestinationId(destinationId)
                    .setDestinationIP(destinationIp)
                    .setSourcePort(sourcePort)
                    .setDestinationPort(destinationPort)
                    .setMessage(ByteString.copyFrom(message))
                    .build();
            tcpTunnelClientEndpoint.send(tunnelCommand);
        }
    }

    public void sendSYN(int sourcePort) throws IOException {
        if (status == OPEN) {
            TunnelProto.TunnelCommand tunnelCommand = TunnelProto.TunnelCommand.newBuilder()
                    .setMethod(TunnelProto.TunnelCommand.Method.CLIENT_SYN)
                    .setSourceType(TunnelProto.TunnelCommand.EndType.CLIENT)
                    .setSourceId(tcpTunnelClientEndpoint.getSessionId())
                    .setDestinationType(TunnelProto.TunnelCommand.EndType.CLIENT)
                    .setDestinationId(destinationId)
                    .setDestinationIP(destinationIp)
                    .setSourcePort(sourcePort)
                    .setDestinationPort(destinationPort)
                    .build();
            tcpTunnelClientEndpoint.send(tunnelCommand);
        }
    }

    public void sendFIN(int sourcePort) throws IOException {
        if (status == OPEN) {
            TunnelProto.TunnelCommand tunnelCommand = TunnelProto.TunnelCommand.newBuilder()
                    .setMethod(TunnelProto.TunnelCommand.Method.CLIENT_FIN)
                    .setSourceType(TunnelProto.TunnelCommand.EndType.CLIENT)
                    .setSourceId(tcpTunnelClientEndpoint.getSessionId())
                    .setDestinationType(TunnelProto.TunnelCommand.EndType.CLIENT)
                    .setDestinationId(destinationId)
                    .setDestinationIP(destinationIp)
                    .setSourcePort(sourcePort)
                    .setDestinationPort(destinationPort)
                    .build();
            tcpTunnelClientEndpoint.send(tunnelCommand);
        }
    }

    public synchronized void closeConnection() {
        status = CLOSE;
    }

    public synchronized void openConnection(String destinationId, String destinationIp, int destinationPort) {
        status = OPEN;
        this.destinationId = destinationId;
        this.destinationPort = destinationPort;
        this.destinationIp = destinationIp;
    }

    @Override
    public void run() {
        LOGGER.info("Thread started");
        try {
            serverBootstrap.bind(listeningPort).sync();
            while (true) {
                if (Thread.interrupted())
                    break;
            }
            shutdown();
        } catch (Exception e) {
            LOGGER.catching(e);
        }
        LOGGER.info("Thread exits");
    }

    public void start() {
        thread.start();
    }

    public void stop() {
        thread.interrupt();
    }

    public int getListeningPort() {
        return listeningPort;
    }

    private void shutdown() {
        try {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            tcpTunnelClientEndpoint.close();
            ProxyClientHandler.closeAllConnections();
        } catch (IOException e) {
            LOGGER.catching(e);
        }
        LOGGER.info("Stopped transport...");
    }
}
