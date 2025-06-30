package dev.datatracks.value;

import com.google.flatbuffers.FlatBufferBuilder;

public class BoolValue implements Value {
    final boolean value;

    BoolValue(boolean value) {
        this.value = value;
    }

    @Override
    public int asFlat(FlatBufferBuilder builder) {
        return protocol.Bool.createBool(builder, value);
    }
}
