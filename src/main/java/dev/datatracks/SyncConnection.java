package dev.datatracks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.flatbuffers.FlatBufferBuilder;
import protocol.Payload;
import protocol.Status;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.function.Consumer;

public class SyncConnection implements Connection {
    private final ObjectMapper mapper = new ObjectMapper();

    private final InetSocketAddress address;
    private final Network network;
    private SocketChannel channel;


    public SyncConnection(Network network ) {
        this.network = network;
        this.address = new InetSocketAddress( network.getHost(), network.getPort() );
    }


    @Override
    public boolean connect() {
        try ( SocketChannel channel = SocketChannel.open( this.address ) ) {
            this.channel = channel;
            channel.connect( this.address );

            FlatBufferBuilder builder = new FlatBufferBuilder( 0 );
            protocol.RegisterRequest.startRegisterRequest(builder);
            var register = protocol.RegisterRequest.endRegisterRequest(builder);

            protocol.OkStatus.startOkStatus(builder);
            var status = protocol.OkStatus.endOkStatus(builder);

            var msg = protocol.Message.createMessage(builder, Payload.RegisterRequest, register, Status.OkStatus, status);

            builder.finish(msg);
            var data = builder.sizedByteArray();

            channel.write(ByteBuffer.wrap(data));
            return true;
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

    @Override
    public void receive(Consumer<Value> consumer) {

    }

}
