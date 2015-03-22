package com.yumcouver.tunnel.client;

import com.yumcouver.tunnel.client.serversocket.ListeningServer;
import com.yumcouver.tunnel.client.util.ConfigReader;
import com.yumcouver.tunnel.client.websocket.TCPTunnelClientEndpoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TCPTunnelClient {
    public static final boolean DEBUG_MODE = false;
    private static final String VERSION = "0.1.0-SNAPSHOT";

    private static final Logger LOGGER = LogManager.getLogger(TCPTunnelClient.class);
    private static final TCPTunnelClient ourInstance = new TCPTunnelClient();

    public static TCPTunnelClient getInstance() {
        return ourInstance;
    }

    private final ListeningServer listeningServer = ListeningServer.getInstance();

    private TCPTunnelClient() {
        listeningServer.start();
    }

    public void closeConnection() {
        System.out.print(">>> ");
        listeningServer.closeConnection();
    }

    public void openConnection(String destinationId, String destinationIp, int port) {
        System.out.print(">>> ");
        listeningServer.openConnection(destinationId, destinationIp, port);
    }

    public void stop() {
        listeningServer.stop();
    }

    public static void main(String[] args) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(System.in));
        String nextLine;
        System.out.format("TCPTunnelClient %s\n" +
                "Type \"help\" for more information.\n", VERSION);

        while (TCPTunnelClientEndpoint.getInstance().getSessionId().equals(
                TCPTunnelClientEndpoint.UNKOWN_ID))
            Thread.sleep(500);
        System.out.format("Get client unique ID: %s\n>>> ",
                TCPTunnelClientEndpoint.getInstance().getSessionId());
        while ((nextLine = bufferedReader.readLine()) != null) {
            if (nextLine.toLowerCase().equals("exit")) {
                System.out.println(">>> ");
                break;
            }
            if (nextLine.toLowerCase().equals("close")) {
                ourInstance.closeConnection();
            } else if (nextLine.toLowerCase().startsWith("open")) {
                String destinationId =
                        nextLine.toLowerCase().substring(5).split("\\s+")[0];
                int port;
                String destinationIp = ConfigReader.DEFAULT_DESTINATION_IP;
                if (nextLine.toLowerCase().substring(5).split("\\s+").length == 2)
                    try {
                        port = Integer.parseInt(
                                nextLine.toLowerCase().substring(5).split("\\s+")[1]);
                    } catch (NumberFormatException e) {
                        System.out.print("invalid port number\n>>> ");
                        continue;
                    }
                else
                    try {
                        destinationIp =
                                nextLine.toLowerCase().substring(5).split("\\s+")[1];
                        port = Integer.parseInt(
                                nextLine.toLowerCase().substring(5).split("\\s+")[2]);
                    } catch (NumberFormatException e) {
                        System.out.print("invalid port number\n>>> ");
                        continue;
                    }
                ourInstance.openConnection(destinationId, destinationIp, port);
            } else if (nextLine.toLowerCase().equals("help")) {
                System.out.print("open\tclose\texit\n>>> ");
            } else if (nextLine.replaceAll("\\\\s+", "").equals(""))
                System.out.print(">>> ");
            else
                System.out.print("Unrecognized input\n>>> ");
        }
        ourInstance.stop();
    }
}

