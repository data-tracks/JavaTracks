package dev.datatracks;

import java.net.SocketException;
import java.net.UnknownHostException;
import lombok.NonNull;

public interface Connection {

    @NonNull
    static Connection initConnection( @NonNull ConnectType type, Network network ) {
        switch ( type ) {
            case UDP:
                try {
                    return new UdpConnection(network);
                } catch ( SocketException | UnknownHostException e ) {
                    throw new RuntimeException( e );
                }
            case TCP:
                return new TcpConnection(network);
        }
        throw new IllegalArgumentException( "Unknown connection type: " + type );
    }

    boolean connect();
    boolean disconnect();
    boolean isConnected();
    boolean isClosed();

    boolean send( Value value );


    enum ConnectType{
        UDP,
        TCP
    }
}
