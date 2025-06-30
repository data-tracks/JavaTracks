package dev.datatracks;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import dev.datatracks.msg.Message;
import dev.datatracks.value.Value;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

public interface Connection extends AutoCloseable {

    @NonNull
    static Connection initConnection(String url, int port) {
        return new SyncConnection(new Network(url, port));
    }

    boolean connect();

    boolean disconnect();

    boolean send(Value value) throws IOException;

    @Nullable Message receive(Duration timeout) throws IOException;

    List<Value> receiveValues(Duration timeout) throws IOException;

    Future<Void> receiveAsync(Consumer<Message> consumer);

    Future<Void> receiveAsyncValues(Consumer<Value> consumer);
}
