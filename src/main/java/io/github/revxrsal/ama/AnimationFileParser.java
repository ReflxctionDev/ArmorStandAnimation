package io.github.revxrsal.ama;

import io.github.revxrsal.ama.internal.AnimationFileTokenizer;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;

/**
 * A utility for parsing animations from <code>.mcfunction</code> files.
 *
 * @see <a href=https://www.youtube.com/watch?v=QrI1A568HvQ>this YouTube video</a>.
 */
public final class AnimationFileParser {

    private AnimationFileParser() {
        throw new AssertionError("Cannot instantiate " + AnimationFileParser.class + "!");
    }

    /**
     * Parses the animation from the specified path, and returns an
     * immutable {@link ArmorStandAnimation} from it.
     *
     * @param path Path to parse from
     * @return The animation
     */
    public static ArmorStandAnimation parse(@NotNull Path path) {
        Objects.requireNonNull(path, "Path is null!");
        return AnimationFileTokenizer.getAnimationFromFile(path);
    }

    /**
     * Parses the animation from the specified file, and returns an
     * immutable {@link ArmorStandAnimation} from it.
     *
     * @param file File to parse from
     * @return The animation
     */
    public static ArmorStandAnimation parse(@NotNull File file) {
        Objects.requireNonNull(file, "file is null!");
        return AnimationFileTokenizer.getAnimationFromFile(file.toPath());
    }
}
