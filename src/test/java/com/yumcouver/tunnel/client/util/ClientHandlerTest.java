package com.yumcouver.tunnel.client.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientHandlerTest {
    public static void main(String args[]) throws IOException {
        ExtendedClientHandler extendedClientHandler = new ExtendedClientHandler();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                System.in));
        String line;
        line = bufferedReader.readLine();
        extendedClientHandler.write(line.getBytes());
        extendedClientHandler.shutdown();
    }
}

class ExtendedClientHandler extends ClientHandler {
    public ExtendedClientHandler() {
        super("127.0.0.1", 12345, new ExtendedClientHandlerAdapter());
    }
}

class ExtendedClientHandlerAdapter extends ClientHandlerAdapter {

    @Override
    public void readEvent(byte[] messageBytes) {

    }

    @Override
    public void connectEvent() {

    }

    @Override
    public void disconnectEvent() {

    }
}
