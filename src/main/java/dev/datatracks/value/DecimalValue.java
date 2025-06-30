package dev.datatracks.value;

import com.google.flatbuffers.FlatBufferBuilder;

import java.math.BigDecimal;

public class DecimalValue implements Value{
    final BigDecimal value;

    DecimalValue(BigDecimal value) {
        this.value = value;
    }


    DecimalValue(float value) {
        this.value = new BigDecimal(value);
    }


    @Override
    public int asFlat(FlatBufferBuilder builder) {
        return protocol.Float.createFloat(builder, value.floatValue() );
    }
}
