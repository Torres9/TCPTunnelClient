package com.yumcouver.tunnel.client.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final Properties props = load();
    public static final String TCP_TUNNEL_SERVER =
            props.getProperty("tcp_tunnel_server");
    public static final int LISTENING_PORT =
            Integer.parseInt(props.getProperty("listening_port"));
    public static final String DEFAULT_DESTINATION_IP =
            props.getProperty("default_destination_ip");

    private static Properties load() {
        Properties props = new Properties();
        try {
            InputStream tmp = ConfigReader.class.getResourceAsStream("/application.properties");
            props.load(tmp);
            tmp.close();
        } catch (IOException e) {
        }
        return props;
    }
}
