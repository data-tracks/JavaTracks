package dev.datatracks;

import com.google.flatbuffers.Table;
import dev.datatracks.value.TextValue;
import dev.datatracks.value.Value;
import org.junit.jupiter.api.Test;
import protocol.Payload;
import protocol.Text;
import protocol.Train;

import java.io.IOException;

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
    public void testReceive() throws InterruptedException {
        Connection connection = Connection.initConnection("localhost", 8686) ;
        assert connection.connect();

        var th = connection.receive((msg -> {
            if (msg.dataType() == Payload.Train) {
                var train = (Train)msg.data(new Train());
                for (int i = 0; i < train.valuesLength(); i++) {
                    var valueWrapper = train.values(i);

                    var dataType = valueWrapper.dataType();
                    if (dataType == protocol.Value.Text) {
                        var text = (Text)valueWrapper.data(new Text());
                        System.out.println(text.data());
                    }
                }
            }
        }));
        Thread.sleep(100000_000);
        th.interrupt();
    }
}
