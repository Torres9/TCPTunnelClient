package com.yumcouver.tunnel.client.websocket;

import com.google.protobuf.ByteString;
import com.yumcouver.tunnel.client.TCPTunnelClient;
import com.yumcouver.tunnel.client.protobuf.TunnelProto;
import com.yumcouver.tunnel.client.serversocket.ProxyClient;
import com.yumcouver.tunnel.client.serversocket.ProxyClientHandler;
import com.yumcouver.tunnel.client.serversocket.RequestHandler;
import com.yumcouver.tunnel.client.util.ConfigReader;
import com.yumcouver.tunnel.client.util.Wireshark;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

@ClientEndpoint
public class TCPTunnelClientEndpoint {
    private static final Logger LOGGER = LogManager.getLogger(TCPTunnelClientEndpoint.class);
    private static final String SEVER_ID = "SERVER-0000-0000-0000-000000000000";
    public static final String UNKOWN_ID = "UNKOWN-FFFF-FFFF-FFFF-FFFFFFFFFFFF";
    private static final String DELIMITER = "-";

    private static TCPTunnelClientEndpoint ourInstance = null;

    private Session session;
    private String sessionId;
    private String prefixOfSessionId;
    private final Wireshark writeStreamWireshark = new Wireshark();
    private final Wireshark readStreamWireshark = new Wireshark();

    public static String getKey(String sessionId, int port) {
        return sessionId + DELIMITER + port;
    }

    public static String getSessionId(String key) {
        return key.substring(0, key.lastIndexOf(DELIMITER));
    }

    public static int getPort(String key) {
        return Integer.parseInt(key.substring(key.lastIndexOf(DELIMITER) + 1));
    }

    public static TCPTunnelClientEndpoint getInstance() {
        if (ourInstance == null)
            ourInstance = new TCPTunnelClientEndpoint(ConfigReader.TCP_TUNNEL_SERVER);
        return ourInstance;
    }

    private TCPTunnelClientEndpoint(String uriStr) {
        setSessionId(UNKOWN_ID);
        ClientManager client = ClientManager.createClient();
        try {
            client.connectToServer(this, new URI(uriStr));
            LOGGER.info("Connected to {}", uriStr);
        } catch (DeploymentException | IOException | URISyntaxException e) {
            LOGGER.catching(e);
        }
    }

    public String getSessionId() {
        return sessionId;
    }

    private void setSessionId(String sessionId) {
        this.sessionId = sessionId;
        this.prefixOfSessionId = sessionId.split("-")[0];
        LOGGER.info("Set session id: {}", prefixOfSessionId);
    }

    private TunnelProto.TunnelCommand errorMessage(String errorCode)
            throws IOException {
        return TunnelProto.TunnelCommand.newBuilder()
                .setMethod(TunnelProto.TunnelCommand.Method.ERROR)
                .setSourceType(TunnelProto.TunnelCommand.EndType.SERVER)
                .setDestinationType(TunnelProto.TunnelCommand.EndType.CLIENT)
                .setMessage(ByteString.copyFromUtf8(errorCode))
                .build();
    }

    private TunnelProto.TunnelCommand sessionIdRequest() {
        return TunnelProto.TunnelCommand.newBuilder()
                .setMethod(TunnelProto.TunnelCommand.Method.ID)
                .setSourceType(TunnelProto.TunnelCommand.EndType.CLIENT)
                .setDestinationType(TunnelProto.TunnelCommand.EndType.SERVER)
                .build();
    }

    public void send(TunnelProto.TunnelCommand tunnelCommand) throws IOException {
        OutputStream outputStream = session.getBasicRemote().getSendStream();
        tunnelCommand.writeTo(outputStream);
        outputStream.close();
        if (TCPTunnelClient.DEBUG_MODE)
            LOGGER.debug("Sent message:\n{}", Wireshark.log(tunnelCommand));
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        this.session = session;
        send(sessionIdRequest());
        LOGGER.info("{} connected to the server", prefixOfSessionId);
    }

    @OnClose
    public void onClose(CloseReason reason) throws IOException {
        LOGGER.info("{} closed, reason: {}", prefixOfSessionId, reason.getCloseCode());
    }

    @OnMessage
    public void onMessage(byte[] response) throws IOException {
        TunnelProto.TunnelCommand tunnelCommand =
                TunnelProto.TunnelCommand.parseFrom(response);
        if (TCPTunnelClient.DEBUG_MODE) {
            LOGGER.debug("Received message:\n{}", Wireshark.log(tunnelCommand));
        }
        int sourcePort = tunnelCommand.getSourcePort();
        String sourceId = tunnelCommand.getSourceId();
        switch (tunnelCommand.getMethod()) {
            case ID:
                setSessionId(tunnelCommand.getMessage().toStringUtf8());
                break;
            case CLIENT_SYN:
                int destinationPort = tunnelCommand.getDestinationPort();
                String destinationIp = "127.0.0.1";
                if(tunnelCommand.hasDestinationIP())
                    destinationIp = tunnelCommand.getDestinationIP();
                new ProxyClient(destinationIp, destinationPort,
                        getKey(sourceId, sourcePort));
                break;
            case SEND:
                ProxyClientHandler proxyClientHandler = ProxyClientHandler.getProxyClientHandler(
                        getKey(sourceId, sourcePort)
                );
                assert proxyClientHandler != null;
                proxyClientHandler.write(tunnelCommand.getMessage().toStringUtf8());
                break;
            case ACK:
                RequestHandler.respond(tunnelCommand.getDestinationPort(),
                        tunnelCommand.getMessage().toStringUtf8());
                break;
            case CLIENT_FIN:
                proxyClientHandler = ProxyClientHandler.getProxyClientHandler(
                        getKey(sourceId, sourcePort)
                );
                assert proxyClientHandler != null;
                proxyClientHandler.close();
                break;
            case ERROR:
                LOGGER.error(tunnelCommand.getMessage().toStringUtf8());
                break;
            default:
                break;
        }
    }

    public void close() throws IOException {
        if (session != null) {
            session.close();
            session = null;
        }
    }
}
