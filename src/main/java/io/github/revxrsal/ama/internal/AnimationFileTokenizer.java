package io.github.revxrsal.ama.internal;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.revxrsal.ama.AnimationFrame;
import io.github.revxrsal.ama.ArmorStandAnimation;
import io.github.revxrsal.ama.Movement3D;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Boolean.parseBoolean;

public final class AnimationFileTokenizer {

    private static final Pattern TICK_SELECTOR = Pattern.compile("@s\\[scores=\\{mc-anim.frame=(?<tick>\\d+)}]");
    private static final Pattern MOVEMENT = Pattern.compile("@s\\[scores=\\{mc-anim\\.frame=(?<tick>\\d+)}] \\^(?<x>-?\\d*\\.?\\d*) \\^(?<y>-?\\d*\\.?\\d*) \\^(?<z>-?\\d*\\.?\\d*)");
    private static final int LOOPING = "# looping: ".length();

    private static final Gson GSON = new GsonBuilder().setLenient()
            .registerTypeAdapter(EulerAngle.class, new EulerAngleAdapter())
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .create();

    public static ArmorStandAnimation getAnimationFromFile(@NotNull Path file) {
        try {
            List<String> lines = Files.readAllLines(file);
            Boolean loop = null;
            ImmutableMap.Builder<Integer, AnimationFrame> frames = new Builder<>();
            Map<Integer, Movement3D> movements = new HashMap<>();
            for (String line : lines) {
                if (loop == null & line.startsWith("# looping: ")) loop = parseBoolean(line.substring(LOOPING));
                else if (line.startsWith("#")) continue;
                if (line.startsWith("data merge")) {
                    String[] parts = line.split("\\s");
                    int tick = parseTickFromString(parts[3]);
                    AnimationFrame frame = GSON.fromJson(parts[4], Pose.class).pose;
                    frames.put(tick, frame);
                } else if (line.startsWith("execute at @s run tp")) {
                    String info = line.substring(21);
                    Matcher matcher = MOVEMENT.matcher(info);
                    if (!matcher.find()) continue;
                    int tick = Integer.parseInt(matcher.group("tick"));
                    double x = Double.parseDouble(matcher.group("x"));
                    double y = Double.parseDouble(matcher.group("y"));
                    double z = Double.parseDouble(matcher.group("z"));
                    movements.put(tick, new Movement3D(x, y, z));
                }
            }
            ImmutableMap<Integer, AnimationFrame> animationFrames = frames.build();
            for (Entry<Integer, Movement3D> me : movements.entrySet()) {
                AnimationFrame frame = animationFrames.get(me.getKey());
                if (frame != null)
                    frame.setMovement(me.getValue());
            }
            if (loop == null) loop = false;
            return new ArmorStandAnimation(loop, animationFrames);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return ArmorStandAnimation.EMPTY;
    }

    private static int parseTickFromString(String string) {
        Matcher m = TICK_SELECTOR.matcher(string);
        if (!m.find())
            return -1;
        return Integer.parseInt(m.group("tick"));
    }

    private static class Pose {

        private AnimationFrame pose;
    }
}
