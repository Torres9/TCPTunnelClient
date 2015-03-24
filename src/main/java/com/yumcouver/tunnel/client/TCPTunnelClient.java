package com.yumcouver.tunnel.client;

import com.yumcouver.tunnel.client.controller.ControllerClientHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Set;

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

    public void join() throws InterruptedException {
        controllerClientHandler.join();
    }

    public static void main(String args[]) throws Exception {
        System.out.println("Press any key to exit.");
        System.in.read();
        ourInstance.shutdown();
        ourInstance.join();
    }
}
