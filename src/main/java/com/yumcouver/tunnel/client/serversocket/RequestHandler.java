package com.yumcouver.tunnel.client.serversocket;

import com.yumcouver.tunnel.client.util.Wireshark;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class RequestHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LogManager.getLogger(RequestHandler.class);
    private static final Map<Integer, RequestHandler> portConnectionMappings =
            new ConcurrentHashMap<>();
    private static final Random random = new Random();
    private static final int MAX_CONNECTION = 100;

    private int port;
    private ChannelHandlerContext ctx;

    public static void respond(int port, byte[] message) {
        RequestHandler requestHandler = portConnectionMappings.get(port);
        if (requestHandler != null) {
            requestHandler.respond(message);
        } else
            LOGGER.warn("Port {} not found in mappings", port);
    }

    // TODO ctx.channel
    public void respond(byte[] message) {
        ByteBuf byteBuf = Unpooled.copiedBuffer(message);
        ctx.write(byteBuf);
        ctx.flush();
        LOGGER.info("Sent message {}", Wireshark.getSubstring(new String(message)));
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        if (portConnectionMappings.size() == MAX_CONNECTION)
            ctx.close();
        while (true) {
            int port = random.nextInt(MAX_CONNECTION - 1) + 1;
            if (!portConnectionMappings.containsKey(port)) {
                this.port = port;
                portConnectionMappings.put(port, this);
                break;
            }
        }
        ListeningServer.getInstance().sendSYN(port);
        // TODO wait until success
        LOGGER.info("Client {} connected", port);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        final ByteBuf in = (ByteBuf) msg;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while (in.isReadable())
            byteArrayOutputStream.write(in.readByte());
        byte[] message = byteArrayOutputStream.toByteArray();
        LOGGER.info("Received message {}", Wireshark.getSubstring(new String(message)));
        ListeningServer.getInstance().send(port, message);
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        portConnectionMappings.remove(port);
        ListeningServer.getInstance().sendFIN(port);
        LOGGER.info("Client {} closed", port);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        portConnectionMappings.remove(port);
        LOGGER.catching(cause);
    }
}
