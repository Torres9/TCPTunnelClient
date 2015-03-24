package com.yumcouver.tunnel.client.controller;

import com.google.protobuf.InvalidProtocolBufferException;
import com.yumcouver.tunnel.client.protobuf.TunnelProto;
import com.yumcouver.tunnel.client.tunnel.TunnelHandler;
import com.yumcouver.tunnel.client.util.ClientHandlerAdapter;
import com.yumcouver.tunnel.client.util.ConfigReader;

public class ControllerClientHandlerAdapter extends ClientHandlerAdapter {
    private boolean initialized = false;

    private static byte[] errorMessage(String errorMessage) {
        TunnelProto.TunnelCommand tunnelCommand = TunnelProto.TunnelCommand.newBuilder()
                .setMethod(TunnelProto.TunnelCommand.Method.ERROR)
                .setMessage(errorMessage)
                .build();
        return tunnelCommand.toByteArray();
    }

    @Override
    public void readEvent(byte[] messageBytes) {
        try {
            TunnelProto.TunnelCommand tunnelCommand =
                    TunnelProto.TunnelCommand.parseFrom(messageBytes);
            switch (tunnelCommand.getMethod()) {
                case CONTROLLER_INIT:
                    assert !initialized;
                    String controllerId = tunnelCommand.getMessage()
                            .split(ControllerClientHandler.DELIMITER)[0];
                    int port = Integer.parseInt(tunnelCommand.getMessage()
                            .split(ControllerClientHandler.DELIMITER)[1]);
                    ControllerClientHandler.getInstance()
                            .setControllerId(controllerId);
                    System.out.format("Forwarding server listening on %s:%d\n",
                            ConfigReader.DESTINATION_HOST, port);
                    initialized = true;
                    break;
                case SYN:
                    assert initialized;
                    TunnelHandler tunnelHandler = new TunnelHandler();
                    ControllerClientHandler.getInstance().addTunnelHandler(tunnelHandler);
                    break;
                default:
                    LOGGER.warn("Unrecognized method");
            }
        } catch (InvalidProtocolBufferException e) {
            LOGGER.catching(e);
        }
    }

    @Override
    public void connectEvent() {
        TunnelProto.TunnelCommand tunnelCommand = TunnelProto.TunnelCommand.newBuilder()
                .setMethod(TunnelProto.TunnelCommand.Method.CONTROLLER_INIT)
                .build();
        write(tunnelCommand.toByteArray());
    }

    @Override
    public void disconnectEvent() {
        ControllerClientHandler.getInstance().shutdown();
    }
}
