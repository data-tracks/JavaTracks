package dev.datatracks.value;

import com.google.flatbuffers.FlatBufferBuilder;

public class NullValue implements Value {
    @Override
    public int asFlat(FlatBufferBuilder builder) {
        protocol.Null.startNull(builder);
        return protocol.Null.endNull(builder);
    }
}
