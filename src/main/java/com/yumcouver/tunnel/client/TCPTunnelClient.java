package com.yumcouver.tunnel.client;

import com.yumcouver.tunnel.client.controller.ControllerClientHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class TCPTunnelClient {
    public static final boolean DEBUG_MODE = false;

    private static final Logger LOGGER = LogManager.getLogger(TCPTunnelClient.class);
    private static final TCPTunnelClient ourInstance = new TCPTunnelClient();

    public static TCPTunnelClient getInstance() {
        return ourInstance;
    }

    private final ControllerClientHandler controllerClientHandler =
            ControllerClientHandler.getInstance();

    public void shutdown() {
        controllerClientHandler.shutdown();
    }

    public static void main(String args[]) throws IOException {
        System.in.read();
        ourInstance.shutdown();
    }
}
