package org.metamechanists.kinematiccore.api.state;

import org.metamechanists.kinematiccore.internal.state.KryoStorage;

import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("unused")
public class StateWriter {
    private final String id;
    private final Map<String, Object> map = new HashMap<>();
    private int version;

    public StateWriter(String id) {
        this.id = id;
    }

    public byte[] toBytes() {
        return KryoStorage.write((kryo, output) -> {
            output.writeString(id);
            output.writeInt(version);

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                output.writeString(entry.getKey());
                kryo.writeClassAndObject(output, entry.getValue());
            }

            output.close();
        });
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void set(String key, Object value) {
        map.put(key, value);
    }
}
