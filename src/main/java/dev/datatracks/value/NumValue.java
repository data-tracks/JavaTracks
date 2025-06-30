package dev.datatracks.value;

import com.google.flatbuffers.FlatBufferBuilder;

public class NumValue implements Value {
    public long value;

    public NumValue(long value) {
        this.value = value;
    }


    @Override
    public int asFlat(FlatBufferBuilder builder) {
        return protocol.Integer.createInteger(builder, value);
    }
}
