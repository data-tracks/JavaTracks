package dev.datatracks;

import com.google.flatbuffers.Table;
import dev.datatracks.value.TextValue;
import dev.datatracks.value.Value;
import org.junit.jupiter.api.Test;
import protocol.Bool;
import protocol.Payload;
import protocol.Text;
import protocol.Train;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

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

        connection.receive(Duration.ofMillis(10));
    }

    @Test
    public void testReceiveValues() throws IOException {
        Connection connection = Connection.initConnection("localhost", 8686) ;
        assert connection.connect();

        connection.receiveValues(Duration.ofMillis(10));
    }

}
