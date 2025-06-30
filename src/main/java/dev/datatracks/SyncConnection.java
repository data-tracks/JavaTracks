package dev.datatracks;

import com.google.flatbuffers.FlatBufferBuilder;
import dev.datatracks.msg.TrainMessage;
import dev.datatracks.value.Value;
import protocol.Message;
import protocol.Payload;
import protocol.Status;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class SyncConnection implements Connection {

    private final InetSocketAddress address;
    private SocketChannel channel;
    ExecutorService executor = Executors.newSingleThreadExecutor();

    public SyncConnection(Network network ) {
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
    public dev.datatracks.msg.Message receive(Duration timeout) throws IOException {
        var future = CompletableFuture.supplyAsync(() -> {
            try {
                return dev.datatracks.msg.Message.from(readMessage());
            } catch (IOException e) {
                return null;
            }
        } );
        future.orTimeout(timeout.toMillis(), TimeUnit.MILLISECONDS);
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    @Override
    public List<Value> receiveValues(Duration timeout) throws IOException {
        var msg = receive(timeout);
        if (msg instanceof TrainMessage train) {
            return train.train.values;
        }
        throw new IOException("Received unknown message from server");
    }

    @Override
    public Future<Void> receiveAsync(Consumer<dev.datatracks.msg.Message> consumer) {
        return executor.submit(() -> {
            try {
                while (true) {
                    consumer.accept(dev.datatracks.msg.Message.from(readMessage()));
                }
            } catch (IOException e) {
                return null;
            }
        });
    }

    @Override
    public Future<Void> receiveAsyncValues(Consumer<Value> consumer) {
       return receiveAsync(msg -> {
           if (msg instanceof TrainMessage train) {
               train.train.values.forEach(consumer);
           }
       });
    }

    @Override
    public void close() throws Exception {
        this.disconnect();
    }
}
