package dev.datatracks;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.function.Consumer;

import lombok.NonNull;

public interface Connection {

    @NonNull
    static Connection initConnection(String url, int port) {
        return new SyncConnection(new Network(url, port));
    }

    boolean connect();

    boolean disconnect();

    boolean isConnected();

    boolean isClosed();

    boolean send(Value value);

    void receive(Consumer<Value> consumer);

    enum ConnectType {
        UDP,
        TCP
    }
}
