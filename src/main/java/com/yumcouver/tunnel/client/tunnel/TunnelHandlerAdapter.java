package com.yumcouver.tunnel.client.tunnel;

import com.yumcouver.tunnel.client.controller.ControllerClientHandler;
import com.yumcouver.tunnel.client.protobuf.TunnelProto;
import com.yumcouver.tunnel.client.proxy.ProxyHandler;
import com.yumcouver.tunnel.client.util.ClientHandlerAdapter;

public class TunnelHandlerAdapter extends ClientHandlerAdapter {
    private ProxyHandler proxyHandler = null;
    private static int count = 0;
    private final int tunnelId;

    public TunnelHandlerAdapter() {
        tunnelId = count;
        count ++;
    }

    public void setProxyHandler(ProxyHandler proxyHandler) {
        this.proxyHandler = proxyHandler;
    }

    @Override
    public void readEvent(byte[] messageBytes) {
        proxyHandler.write(messageBytes);
    }

    @Override
    public void connectEvent() {
        while (proxyHandler == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                LOGGER.catching(e);
            }
        }
        while(!proxyHandler.isConnectionRefused() && !proxyHandler.isConnected()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                LOGGER.catching(e);
            }
        }
        if(proxyHandler.isConnectionRefused())
            proxyHandler.shutdown();
        TunnelProto.TunnelCommand tunnelCommand = TunnelProto.TunnelCommand.newBuilder()
                .setMethod(TunnelProto.TunnelCommand.Method.TUNNEL_INIT)
                .setMessage(ControllerClientHandler.getInstance().getUniqueId(tunnelId))
                .build();
        write(tunnelCommand.toByteArray());
    }

    @Override
    public void disconnectEvent() {
        proxyHandler.shutdown();
        while(isConnected() || proxyHandler.isConnected()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                LOGGER.catching(e);
            }
        }
    }

    @Override
    public void shutdownEvent() {
    }
}
