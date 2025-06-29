package dev.datatracks.value;

import com.google.flatbuffers.FlatBufferBuilder;

public class IntValue implements Value {
    public int value;

    public IntValue(int value) {
        this.value = value;
    }


    @Override
    public int asFlat(FlatBufferBuilder builder) {
        return protocol.Integer.createInteger(builder, value);
    }
}
