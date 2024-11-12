package dev.datatracks;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UdpConnection implements Connection {

    private final Network network;
    private final DatagramSocket socket;
    private static final ObjectMapper mapper =  new ObjectMapper();
    private final InetAddress address;


    UdpConnection( Network network) throws SocketException, UnknownHostException {
        this.network = network;
        this.socket = new DatagramSocket();
        this.address = InetAddress.getByName( network.getHost() );
    }


    @Override
    public boolean connect() {
        if ( socket != null && socket.isConnected() ) {
            socket.close();
        }
        return true;
    }


    @Override
    public boolean disconnect() {
        return true;
    }


    @Override
    public boolean isConnected() {
        return false;
    }


    @Override
    public boolean isClosed() {
        return false;
    }


    @Override
    public boolean send( Value value ) {
        DatagramPacket packet = new DatagramPacket(value.getData(), value.getData().length, this.address , network.getPort());
        try {
            socket.send(packet);
        } catch ( IOException e ) {
            return false;
        }
        return true;
    }

}
