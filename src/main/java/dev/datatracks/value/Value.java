package dev.datatracks.value;

import com.google.flatbuffers.FlatBufferBuilder;

public interface Value {

    int asFlat(FlatBufferBuilder builder);

    static Value text(String value) {
        return new TextValue(value);
    }

    static Value num(int value) {
        return new IntValue(value);
    }

}
