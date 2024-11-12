package dev.datatracks;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class TcpConnection implements Connection {
    private final ObjectMapper mapper = new ObjectMapper();

    private final InetSocketAddress address;
    private final Network network;
    private SocketChannel channel;


    public TcpConnection( Network network ) {
        this.network = network;
        this.address = new InetSocketAddress( network.getHost(), network.getPort() );
    }


    @Override
    public boolean connect() {
        try ( SocketChannel channel = SocketChannel.open( this.address ) ) {
            this.channel = channel;
            return channel.connect( this.address );
        } catch ( IOException e ) {
            return false;
        }
    }


    @Override
    public boolean disconnect() {
        try {
            this.channel.close();
        } catch ( IOException e ) {
            return false;
        }
        this.channel = null;
        return true;
    }


    @Override
    public boolean isConnected() {
        return this.channel != null && this.channel.isConnected();
    }


    @Override
    public boolean isClosed() {
        return this.channel == null;
    }


    @Override
    public boolean send( Value value ) {
        return false;
    }

}
