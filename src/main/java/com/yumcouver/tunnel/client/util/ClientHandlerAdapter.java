package com.yumcouver.tunnel.client.util;

import com.yumcouver.tunnel.client.TCPTunnelClient;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;

public abstract class ClientHandlerAdapter extends ChannelInboundHandlerAdapter {
    protected final Logger LOGGER = LogManager.getLogger(this.getClass());
    private ChannelHandlerContext ctx = null;
    protected boolean connectionRefused = false;

    public void shutdown() {
        synchronized (this) {
            if (ctx != null) {
                ctx.close();
                ctx = null;
            }
        }
        shutdownEvent();
    }

    public void write(byte[] messageBytes) {
        synchronized (this) {
            if (ctx != null) {
                final ByteBuf messageByteBuf = Unpooled.copiedBuffer(messageBytes);
                ctx.write(messageByteBuf);
                ctx.flush();
                if (TCPTunnelClient.DEBUG_MODE)
                    LOGGER.debug("Sent message: {}", Wireshark.getSubstring(messageBytes));
            } else
                LOGGER.warn("Not connected");
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        synchronized (this) {
            this.ctx = ctx;
        }
        InetSocketAddress inetSocketAddress =
                (InetSocketAddress) ctx.channel().remoteAddress();
        connectEvent();
        LOGGER.info("Connected to {}:{}", inetSocketAddress.getHostString(),
                inetSocketAddress.getPort());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        synchronized (this) {
            this.ctx = null;
        }
        InetSocketAddress inetSocketAddress =
                (InetSocketAddress) ctx.channel().remoteAddress();
        disconnectEvent();
        LOGGER.info("Disconnected to {}:{}", inetSocketAddress.getHostString(),
                inetSocketAddress.getPort());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        final ByteBuf messageByteBuf = (ByteBuf) msg;
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while (messageByteBuf.isReadable())
            byteArrayOutputStream.write(messageByteBuf.readByte());
        final byte[] messageBytes = byteArrayOutputStream.toByteArray();
        if (TCPTunnelClient.DEBUG_MODE)
            LOGGER.debug("Received message: {}", Wireshark.getSubstring(messageBytes));
        readEvent(messageBytes);
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        synchronized (this) {
            this.ctx = null;
        }
        LOGGER.catching(cause);
        ctx.close();
    }

    public boolean isConnected() {
        synchronized (this) {
            return this.ctx != null;
        }
    }

    public void setConnectionRefused() {
        connectionRefused = true;
    }

    public boolean isConnectionRefused() {
        return connectionRefused;
    }

    public abstract void readEvent(byte[] messageBytes);

    public abstract void connectEvent();

    public abstract void disconnectEvent();

    public abstract void shutdownEvent();
}
