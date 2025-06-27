package dev.datatracks;

import java.net.InetSocketAddress;
import lombok.Value;

@Value
public class Network {
    String host;
    int port;
    InetSocketAddress inetAddress;


    public Network(String host, int port) {
        this.host = host;
        this.port = port;
        this.inetAddress = new InetSocketAddress(host, port);
    }
}

