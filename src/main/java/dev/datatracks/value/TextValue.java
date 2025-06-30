package dev.datatracks.value;

import com.google.flatbuffers.FlatBufferBuilder;

public class TextValue implements Value {
    public final String value;

    TextValue(String value) {
        this.value = value;
    }



    @Override
    public int asFlat(FlatBufferBuilder builder) {
        var text = builder.createString(this.value);
        return protocol.Text.createText(builder, text);
    }
}
