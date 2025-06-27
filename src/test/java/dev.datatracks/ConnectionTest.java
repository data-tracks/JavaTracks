package dev.datatracks;

import org.junit.jupiter.api.Test;

public class ConnectionTest {

    @Test
    public void testConnection() {
        Connection connection = Connection.initConnection("localhost", 5959) ;
        connection.connect();
    }
}
