package com.yumcouver.tunnel.client.controller;

import com.yumcouver.tunnel.client.tunnel.TunnelHandler;
import com.yumcouver.tunnel.client.util.ClientHandler;
import com.yumcouver.tunnel.client.util.ConfigReader;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ControllerClientHandler extends ClientHandler {
    private static final String DELIMITER = "-";
    private static final ControllerClientHandler ourInstance = new ControllerClientHandler();
    private static final List<TunnelHandler> tunnelHandlers = Collections.synchronizedList(
            new LinkedList<TunnelHandler>());

    private String controllerId = "UNKNOWN";

    private ControllerClientHandler() {
        super(ConfigReader.TCP_TUNNEL_SERVER_HOST,
                ConfigReader.TCP_TUNNEL_SERVER_PORT,
                new ControllerClientHandlerAdapter());
    }

    public static ControllerClientHandler getInstance() {
        return ourInstance;
    }

    public String getUniqueId(int tunnelId) {
        return controllerId + DELIMITER + tunnelId;
    }

    public void setControllerId(String controllerId) {
        LOGGER.info("Set controller id: {}", controllerId);
        this.controllerId = controllerId;
    }

    @Override
    public synchronized void shutdown() {
        while (clientHandlerAdapter.isConnected()) {
            clientHandlerAdapter.shutdown();
            workerGroup.shutdownGracefully();
        }
        for (TunnelHandler tunnelHandler : tunnelHandlers) {
            while (tunnelHandler.isConnected())
                tunnelHandler.shutdown();
        }
    }

    public void addTunnelHandler(TunnelHandler tunnelHandler) {
        tunnelHandlers.add(tunnelHandler);
    }

    public void removeTunnelHandler(TunnelHandler tunnelHandler) {
        tunnelHandlers.remove(tunnelHandler);
    }
}
