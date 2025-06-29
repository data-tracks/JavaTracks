package dev.datatracks;

import java.io.IOException;
import java.util.function.Consumer;

import dev.datatracks.value.Value;
import lombok.NonNull;
import protocol.Message;

public interface Connection {

    @NonNull
    static Connection initConnection(String url, int port) {
        return new SyncConnection(new Network(url, port));
    }

    boolean connect();

    boolean disconnect();

    boolean isConnected();

    boolean isClosed();

    boolean send(Value value) throws IOException;

    Thread receive(Consumer<Message> consumer);

    enum ConnectType {
        UDP,
        TCP
    }
}
