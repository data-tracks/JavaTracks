package dev.datatracks.msg;

import dev.datatracks.Train;

public class TrainMessage extends Message {
    public final Train train;

    public TrainMessage(protocol.Message message) {
        var train = message.data(new protocol.Train());
        if (train != null) {
            this.train = new Train((protocol.Train) train);
        }else {
            this.train = null;
        }
    }
}
