package dev.datatracks;

import java.net.InetSocketAddress;
import lombok.Value;

@Value
public class Network {
    String host;
    int port;
    InetSocketAddress inetAddress;

}

