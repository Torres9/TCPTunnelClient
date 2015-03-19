package com.yumcouver.tunnel.client.serversocket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    public static void respond(int port, String string) {
        RequestHandler requestHandler = portConnectionMappings.get(port);
        if (requestHandler != null) {
            requestHandler.respond(string);
        } else
            LOGGER.warn("Port {} not found in mappings", port);
    }

    // TODO ctx.channel
    public void respond(String string) {
        ChannelFuture cf = ctx.write(Unpooled.copiedBuffer(string, CharsetUtil.UTF_8));
        ctx.flush();
        LOGGER.info("Sent message {}", string);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        if(portConnectionMappings.size() == MAX_CONNECTION)
            ctx.close();
        while(true) {
            int port = random.nextInt(MAX_CONNECTION);
            if(!portConnectionMappings.containsKey(port)) {
                this.port = port;
                portConnectionMappings.put(port, this);
                break;
            }
        }
        ListeningServer.getInstance().sendSYN(port);
        LOGGER.info("Client {} connected", port);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        final ByteBuf in = (ByteBuf) msg;
        final byte[] messageBytes = in.toString(io.netty.util.CharsetUtil.US_ASCII).getBytes();
        ListeningServer.getInstance().send(port, messageBytes);
        LOGGER.info("Received message {}", new String(messageBytes));
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
