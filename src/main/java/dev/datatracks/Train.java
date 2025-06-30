package dev.datatracks;

import dev.datatracks.value.Value;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Train {
    public final String topic;
    public final Instant eventTime;
    public final List<Value> values;

    public Train(protocol.Train train) {
        this.topic = train.topic();
        var eventTime = train.eventTime();
        this.eventTime = eventTime != null ? Instant.ofEpochMilli(eventTime.data()) : null;
        var values = new ArrayList<Value>();
        for (int i = 0; i < train.valuesLength(); i++) {
            values.add(Value.from(train.values(i)));
        }

        this.values = values;
    }
}
