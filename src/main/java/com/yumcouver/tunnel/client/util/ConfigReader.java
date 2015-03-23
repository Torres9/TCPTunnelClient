package com.yumcouver.tunnel.client.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    final static Logger LOGGER = LogManager.getLogger(ConfigReader.class);

    private static final Properties props = load();
    public static final String TCP_TUNNEL_SERVER_HOST =
            props.getProperty("tcp_tunnel_server_host");
    public static final int TCP_TUNNEL_SERVER_PORT =
            Integer.parseInt(props.getProperty("tcp_tunnel_server_port"));
    public static final String DESTINATION_HOST =
            props.getProperty("destination_host");
    public static final int DESTINATION_PORT =
            Integer.parseInt(props.getProperty("destination_port"));

    private static Properties load() {
        Properties props = new Properties();
        try {
            InputStream tmp = ConfigReader.class.getResourceAsStream("/application.properties");
            if(tmp == null) {
                LOGGER.fatal("application.properties not found");
            }
            else {
                props.load(tmp);
                tmp.close();
            }
        } catch (IOException e) {
            LOGGER.catching(e);
        }
        return props;
    }
}