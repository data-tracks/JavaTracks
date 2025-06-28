package dev.datatracks;

import org.junit.jupiter.api.Test;

public class ConnectionTest {

    @Test
    public void testConnection() {
        Connection connection = Connection.initConnection("::1", 5959) ;
        assert connection.connect();
    }
}
