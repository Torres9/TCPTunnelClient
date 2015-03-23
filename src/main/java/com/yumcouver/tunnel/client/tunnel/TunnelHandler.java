package com.yumcouver.tunnel.client.tunnel;

import com.yumcouver.tunnel.client.proxy.ProxyHandler;
import com.yumcouver.tunnel.client.util.ClientHandler;
import com.yumcouver.tunnel.client.util.ConfigReader;

import java.io.IOException;

public class TunnelHandler extends ClientHandler {
    private ProxyHandler proxyHandler;

    public TunnelHandler() {
        super(ConfigReader.TCP_TUNNEL_SERVER_HOST,
                ConfigReader.TCP_TUNNEL_SERVER_PORT,
                new TunnelHandlerAdapter());
        if(!clientHandlerAdapter.isConnectionRefused()) {
            proxyHandler = new ProxyHandler(this);
            ((TunnelHandlerAdapter) clientHandlerAdapter).setProxyHandler(proxyHandler);
        }
    }

    @Override
    public synchronized void shutdown() {
        while(clientHandlerAdapter.isConnected()) {
            clientHandlerAdapter.shutdown();
            workerGroup.shutdownGracefully();
        }
        while(proxyHandler != null && proxyHandler.isConnected()) {
            proxyHandler.shutdown();
        }
    }

    public static void main(String args[]) throws IOException {
        new TunnelHandler();
        System.in.read();
    }
}
