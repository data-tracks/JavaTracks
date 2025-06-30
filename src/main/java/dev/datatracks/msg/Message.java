package dev.datatracks.msg;

import org.jetbrains.annotations.Nullable;
import protocol.Payload;

public abstract class Message {

    @Nullable
    public static Message from(protocol.Message message) {
        if (message.dataType() == Payload.Train){
            return new TrainMessage(message);
        }else if (message.dataType() == Payload.RegisterResponse) {
            return new RegisterResponseMessage(message);
        }
        return null;
    }

}

