package com.yumcouver.tunnel.client.controller;

import com.yumcouver.tunnel.client.util.ClientHandler;
import com.yumcouver.tunnel.client.util.ConfigReader;

public class ControllerClientHandler extends ClientHandler{
    private static final String DELIMITER = "-";
    private static ControllerClientHandler ourInstance = new ControllerClientHandler();

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
        LOGGER.info("Set controller id to {}", controllerId);
        this.controllerId = controllerId;
    }
}
