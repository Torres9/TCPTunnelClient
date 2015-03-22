package com.yumcouver.tunnel.client.serversocket;

import com.google.protobuf.ByteString;
import com.yumcouver.tunnel.client.protobuf.TunnelProto;
import com.yumcouver.tunnel.client.util.Wireshark;
import com.yumcouver.tunnel.client.websocket.TCPTunnelClientEndpoint;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProxyClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LogManager.getLogger(ProxyClientHandler.class);
    private static Map<String, ProxyClientHandler> keyProxyClientHandlerMappings =
            new ConcurrentHashMap<>();

    private final String key;
    private final ProxyClient proxyClient;
    private ChannelHandlerContext ctx;

    public static ProxyClientHandler getProxyClientHandler(String key) {
        ProxyClientHandler proxyClientHandler = keyProxyClientHandlerMappings.get(key);
        if (proxyClientHandler == null)
            LOGGER.warn("key {} not found in mappings", key);
        return proxyClientHandler;
    }

    public static void close(String key) {
        ProxyClientHandler proxyClientHandler = keyProxyClientHandlerMappings.get(key);
        if (proxyClientHandler == null)
            LOGGER.warn("key {} not found in mappings", key);
        else
            proxyClientHandler.close();
    }

    public static void closeAllConnections() {
        for (ProxyClientHandler proxyClientHandler : keyProxyClientHandlerMappings.values())
            proxyClientHandler.close();
        keyProxyClientHandlerMappings =
                new ConcurrentHashMap<>();
    }

    public ProxyClientHandler(String key, ProxyClient proxyClient) {
        this.key = key;
        this.proxyClient = proxyClient;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("Proxy connected");
        keyProxyClientHandlerMappings.put(key, this);
        this.ctx = ctx;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        keyProxyClientHandlerMappings.remove(key);
        ctx.close();
        proxyClient.close();
        LOGGER.catching(cause);
    }

    public void close() {
        keyProxyClientHandlerMappings.remove(key);
        ctx.close();
        proxyClient.close();
        LOGGER.info("Porxy Client closed");
    }

    public void write(byte[] message) {
        final ByteBuf byteBuf = ctx.alloc().buffer(message.length);
        byteBuf.writeBytes(message);
        final ChannelFuture f = ctx.writeAndFlush(byteBuf);
        f.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                assert f == future;
            }
        });
        LOGGER.info("Sent message {}", Wireshark.getSubstring(new String(message)));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        final ByteBuf byteBuf = (ByteBuf) msg;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        while (byteBuf.isReadable()) {
            byteArrayOutputStream.write(byteBuf.readByte());
        }
        byte[] message = byteArrayOutputStream.toByteArray();
        LOGGER.info("Received message {}", Wireshark.getSubstring(new String(message)));
        TunnelProto.TunnelCommand tunnelCommand = TunnelProto.TunnelCommand.newBuilder()
                .setMethod(TunnelProto.TunnelCommand.Method.ACK)
                .setSourceType(TunnelProto.TunnelCommand.EndType.CLIENT)
                .setSourceId(TCPTunnelClientEndpoint.getInstance().getSessionId())
                .setSourcePort(proxyClient.getPort())
                .setDestinationType(TunnelProto.TunnelCommand.EndType.CLIENT)
                .setDestinationId(TCPTunnelClientEndpoint.getSessionId(key))
                .setDestinationPort(TCPTunnelClientEndpoint.getPort(key))
                .setMessage(ByteString.copyFrom(message))
                .build();
        TCPTunnelClientEndpoint.getInstance().send(tunnelCommand);
        ReferenceCountUtil.release(msg);
    }
}
