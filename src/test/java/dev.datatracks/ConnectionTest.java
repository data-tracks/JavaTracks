package dev.datatracks;

import dev.datatracks.msg.Message;
import dev.datatracks.value.Value;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;

public class ConnectionTest {

    @Test
    public void testConnection() {
        Connection connection = Connection.initConnection("::1", 5959) ;
        assert connection.connect();
    }


    @Test
    public void testSend() throws IOException {
        Connection connection = Connection.initConnection("localhost", 9999) ;
        assert connection.connect();
        assert connection.send(Value.text("test"));
    }


    @Test
    public void testReceiveMsg() throws IOException {
        Connection connection = Connection.initConnection("localhost", 8686) ;
        assert connection.connect();

        connection.receive(Duration.ofMillis(1_000));
    }

    @Test
    public void testReceiveValues() throws IOException {
        Connection connection = Connection.initConnection("localhost", 8686) ;
        assert connection.connect();

        connection.receiveValues(Duration.ofMillis(2_000));
    }

    @Test
    public void testReceiveAsync() throws IOException, InterruptedException {
        Connection connection = Connection.initConnection("localhost", 8686) ;
        assert connection.connect();

        var trains = new ArrayList<Message>();
        var future = connection.receiveAsync(trains::add);
        Thread.sleep(2_000);
        future.cancel(true);
        assert !trains.isEmpty();

        connection.disconnect();
    }

    @Test
    public void testReceiveValuesAsync() throws IOException, InterruptedException {
        try (Connection connection = Connection.initConnection("localhost", 8686) ) {
            assert connection.connect();

            var values = new ArrayList<Value>();
            var future = connection.receiveAsyncValues(values::add);
            Thread.sleep(2_000);
            future.cancel(true);
            assert !values.isEmpty();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
