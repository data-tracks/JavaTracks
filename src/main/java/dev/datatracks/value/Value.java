package dev.datatracks.value;

import com.google.flatbuffers.FlatBufferBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import protocol.Text;
import protocol.ValueWrapper;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public interface Value {

    static Value from(protocol.ValueWrapper value) {
        if (value.dataType() == protocol.Value.Text) {
            return Value.text(value);
        }else if (value.dataType() == protocol.Value.Integer) {
            return Value.num(value);
        }else if (value.dataType() == protocol.Value.Float) {
            return Value.decimal(value);
        }else if (value.dataType() == protocol.Value.Bool) {
            return Value.bool(value);
        }
        throw new IllegalArgumentException("Unknown Value: " + value.dataType());
    }

    int asFlat(FlatBufferBuilder builder);

    @NotNull
    static Value text(String value) {
        return new TextValue(value);
    }

    @NotNull
    static Value text( @Nullable protocol.Text value) {
        if (value == null || value.data() == null) {
            return new NullValue();
        }
        return new TextValue(value.data());
    }

    static Value text(ValueWrapper valueWrapper) {
        var text = (protocol.Text)valueWrapper.data(new protocol.Text());
        return text(text);
    }

    @NotNull
    static Value num(int value) {
        return new NumValue(value);
    }

    @NotNull
    static Value num(@Nullable protocol.Integer value) {
        if (value == null) {
            return new NullValue();
        }
        return new NumValue(value.data());
    }

    static Value num(ValueWrapper valueWrapper) {
        var num = (protocol.Integer)valueWrapper.data(new protocol.Integer());
        return num(num);
    }

    @NotNull
    static Value bool(boolean value) {
        return new BoolValue(value);
    }


    @NotNull
    static Value bool(@Nullable Boolean value) {
        if (value == null) {
            return new NullValue();
        }
        return new BoolValue(value);
    }

    @NotNull
    static Value bool(@Nullable protocol.Bool bool) {
        if (bool == null) {
            return new NullValue();
        }
        return new BoolValue(bool.data());
    }

    static Value bool(ValueWrapper valueWrapper) {
        var bool = (protocol.Bool)valueWrapper.data(new protocol.Bool());
        return bool(bool);
    }

    @NotNull
    static Value decimal(float value){
        return new DecimalValue(value);
    }

    @NotNull
    static Value decimal(@Nullable protocol.Float value) {
        if (value == null) {
            return new NullValue();
        }
        return new DecimalValue(value.data());
    }

    static Value decimal(ValueWrapper valueWrapper) {
        var decimal = (protocol.Float)valueWrapper.data(new protocol.Float());
        return decimal(decimal);
    }

    static List<Value> extractValues(protocol.Message msg) {
        var values = new ArrayList<Value>();
        var train = (protocol.Train)msg.data(new protocol.Train());

        if (train == null){
            return values;
        }

        for (int i = 0; i < train.valuesLength(); i++) {
            var valueWrapper = train.values(i);

            var dataType = valueWrapper.dataType();
            if (dataType == protocol.Value.Text) {
                values.add(Value.text(valueWrapper));
            }else if (dataType == protocol.Value.Bool) {
                var bool = (protocol.Bool)valueWrapper.data(new protocol.Bool());
                values.add(Value.bool(bool));
            }else if (dataType == protocol.Value.Float) {
                var bool = (protocol.Bool)valueWrapper.data(new protocol.Bool());
                values.add(Value.bool(bool));
            }
        }
        return values;
    }

}
