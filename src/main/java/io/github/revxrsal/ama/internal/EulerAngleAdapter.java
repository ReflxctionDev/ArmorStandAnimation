package io.github.revxrsal.ama.internal;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bukkit.util.EulerAngle;

import java.io.IOException;

/**
 * A custom deserialization strategy for {@link EulerAngle}s
 */
public class EulerAngleAdapter extends TypeAdapter<EulerAngle> {

    @Override public void write(JsonWriter out, EulerAngle value) throws IOException {
        out.beginArray();
        out.value(value.getX());
        out.value(value.getY());
        out.value(value.getZ());
        out.endArray();
    }

    @Override public EulerAngle read(JsonReader in) throws IOException {
        in.beginArray();
        double x = Math.toRadians(in.nextDouble());
        double y = Math.toRadians(in.nextDouble());
        double z = Math.toRadians(in.nextDouble());
        in.endArray();
        return new EulerAngle(x, y, z);
    }
}
