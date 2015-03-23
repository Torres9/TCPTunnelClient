package com.yumcouver.tunnel.client.proxy;

import com.yumcouver.tunnel.client.tunnel.TunnelHandler;
import com.yumcouver.tunnel.client.util.ClientHandlerAdapter;

public class ProxyHandlerAdapter extends ClientHandlerAdapter {
    private final TunnelHandler tunnelHandler;

    public ProxyHandlerAdapter(TunnelHandler tunnelHandler) {
        this.tunnelHandler = tunnelHandler;
    }

    @Override
    public void readEvent(byte[] messageBytes) {
        tunnelHandler.write(messageBytes);
    }

    @Override
    public void connectEvent() {
        while(!tunnelHandler.isConnectionRefused() && !tunnelHandler.isConnected()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                LOGGER.catching(e);
            }
        }
        if(tunnelHandler.isConnectionRefused())
            tunnelHandler.shutdown();
    }

    @Override
    public void disconnectEvent() {
        tunnelHandler.shutdown();
        while(isConnected() || tunnelHandler.isConnected()) {
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
