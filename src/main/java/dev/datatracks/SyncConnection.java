package dev.datatracks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.flatbuffers.FlatBufferBuilder;
import dev.datatracks.value.Value;
import protocol.Message;
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
        try {
            this.channel = SocketChannel.open() ;
            this.channel.connect(this.address);

            var data = getRegister();

            var len = ByteBuffer.allocate(4).putInt(data.length).array();
            this.channel.write(ByteBuffer.wrap(len));
            this.channel.write(ByteBuffer.wrap(data));
            protocol.Message response = readMessage();


            return true;
        } catch ( IOException e ) {
            return false;
        }
    }

    private protocol.Message readMessage() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        this.channel.read(buffer);
        var length = buffer.flip().getInt();

        ByteBuffer byteBuffer = ByteBuffer.allocate(length);
        this.channel.read(byteBuffer);
        return Message.getRootAsMessage(byteBuffer.flip());
    }

    private static byte[] getRegister() {
        FlatBufferBuilder builder = new FlatBufferBuilder( 0 );
        protocol.RegisterRequest.startRegisterRequest(builder);
        var register = protocol.RegisterRequest.endRegisterRequest(builder);

        protocol.OkStatus.startOkStatus(builder);
        var status = protocol.OkStatus.endOkStatus(builder);

        var msg = protocol.Message.createMessage(builder, Payload.RegisterRequest, register, Status.OkStatus, status);

        builder.finish(msg);
        return builder.sizedByteArray();
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
    public boolean send( Value value ) throws IOException {
        if ( channel == null ) {
            this.connect();
        }

        var builder = new FlatBufferBuilder();

        var topic = builder.createSharedString("");
        var evenTime = protocol.Time.createTime(builder, System.currentTimeMillis()); // todo maybe change
        var train = protocol.Train.createTrain(builder, value.asFlat(builder), topic, evenTime );
        protocol.OkStatus.startOkStatus(builder);
        var status = protocol.OkStatus.endOkStatus(builder);
        var msg = protocol.Message.createMessage(builder, Payload.Train, train, Status.OkStatus, status );

        builder.finish(msg);
        writeAll(builder.sizedByteArray());
        return true;
    }

    private void writeAll(byte[] data) throws IOException {
        var len = ByteBuffer.allocate(4).putInt(data.length).array();
        channel.write(ByteBuffer.wrap(len));
        channel.write(ByteBuffer.wrap(data));
    }

    @Override
    public Thread receive(Consumer<Message> consumer) {
        var th = new Thread(() -> {
            try {
                while (true) {
                    consumer.accept(readMessage());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        th.start();
        return th;
    }

}
