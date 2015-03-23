package com.yumcouver.tunnel.client.controller;

import com.google.protobuf.InvalidProtocolBufferException;
import com.yumcouver.tunnel.client.protobuf.TunnelProto;
import com.yumcouver.tunnel.client.tunnel.TunnelHandler;
import com.yumcouver.tunnel.client.util.ClientHandlerAdapter;

public class ControllerClientHandlerAdapter extends ClientHandlerAdapter{
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
                    ControllerClientHandler.getInstance()
                            .setControllerId(tunnelCommand.getMessage());
                    initialized = true;
                    break;
                case SYN:
                    assert initialized;
                    new TunnelHandler();
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

    @Override
    public void shutdownEvent() {

    }
}
