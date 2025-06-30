package dev.datatracks;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import dev.datatracks.msg.Message;
import dev.datatracks.value.Value;
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

    boolean send(Value value) throws IOException;

    Message receive(Duration timeout) throws IOException;

    List<Value> receiveValues(Duration timeout) throws IOException;

    enum ConnectType {
        UDP,
        TCP
    }
}
