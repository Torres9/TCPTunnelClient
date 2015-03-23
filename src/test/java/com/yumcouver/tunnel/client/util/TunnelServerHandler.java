package com.yumcouver.tunnel.client.util;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ReferenceCountUtil;

import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;

public class TunnelServerHandler {
    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new TunnelServerHandlerAdapter());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        b.bind(8511);
        System.in.read();
    }
}

class TunnelServerHandlerAdapter extends ChannelInboundHandlerAdapter {

    private ChannelHandlerContext ctx = null;
    private int count = 1;

    public void write() {
        String message = generateString();
        message = (char)message.length() + message;
        final ByteBuf messageByteBuf = Unpooled.copiedBuffer(message.getBytes());
        ctx.write(messageByteBuf);
        ctx.flush();
        System.out.format("Sent message %s\n", Wireshark.getSubstring(message));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        InetSocketAddress inetSocketAddress =
                (InetSocketAddress) ctx.channel().remoteAddress();
        System.out.format("%s:%d connected\n", inetSocketAddress.getHostString(),
                inetSocketAddress.getPort());
        write();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        this.ctx = null;
        InetSocketAddress inetSocketAddress =
                (InetSocketAddress) ctx.channel().remoteAddress();
        System.out.format("%s:%d disconnected\n", inetSocketAddress.getHostString(),
                inetSocketAddress.getPort());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        count ++;
        if(count == 10)
            ctx.close();
        final ByteBuf messageByteBuf = (ByteBuf) msg;
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while (messageByteBuf.isReadable())
            byteArrayOutputStream.write(messageByteBuf.readByte());
        final byte[] messageBytes = byteArrayOutputStream.toByteArray();
        System.out.println("Received: "+new String(messageBytes));
        write();
        ReferenceCountUtil.release(msg);
    }

    public static String generateString() {
        final char[] CHARS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
            'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
            'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
        final int MIN_NUM = 20;
        final int MAX_NUM = 40;

        final int randomNum = MIN_NUM + (int)(Math.random()*MAX_NUM);
        final StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < randomNum; i++) {
            int randomIndex = (int)(Math.random()*CHARS.length);
            stringBuilder.append(CHARS[randomIndex]);
        }
        return stringBuilder.toString();
    }
}

