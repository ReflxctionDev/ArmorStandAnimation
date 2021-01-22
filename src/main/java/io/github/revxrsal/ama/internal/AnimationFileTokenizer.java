package io.github.revxrsal.ama.internal;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.revxrsal.ama.AnimationFrame;
import io.github.revxrsal.ama.ArmorStandAnimation;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Boolean.parseBoolean;

public final class AnimationFileTokenizer {

    private static final Pattern TICK_SELECTOR = Pattern.compile("@s\\[scores=\\{mc-anim.frame=(?<tick>\\d+)}]");
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
            for (String line : lines) {
                if (loop == null & line.startsWith("# looping: ")) loop = parseBoolean(line.substring(LOOPING));
                else if (line.startsWith("#")) continue;
                if (line.startsWith("data merge")) {
                    String[] parts = line.split("\\s");
                    int tick = parseTickFromString(parts[3]);
                    AnimationFrame frame = GSON.fromJson(parts[4], Pose.class).pose;
                    frames.put(tick, frame);
                }
            }
            if (loop == null) loop = false;
            return new ArmorStandAnimation(loop, frames.build());
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
