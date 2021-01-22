package io.github.revxrsal.ama;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an armor stand animation.
 * <p>
 * This class is immutable, hence is thread-safe.
 */
@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ArmorStandAnimation {

    /**
     * Represents an empty armor stand animation.
     */
    public static final ArmorStandAnimation EMPTY = new ArmorStandAnimation(false, ImmutableMap.of());

    /**
     * Whether is the animation looping or not
     */
    private final boolean looping;

    /**
     * All the animation frames and their ticks.
     */
    private final ImmutableMap<Integer, AnimationFrame> frames;

    /**
     * Plays this animation by running a task for that plugin that moves the armor stand
     *
     * @param plugin     Plugin to run on
     * @param armorStand Armor stand to animate
     * @return An animation controller for this animation
     * @see #playAsync(Plugin, ArmorStand)
     * @see AnimationController
     */
    public @NotNull AnimationController play(@NotNull Plugin plugin, @NotNull ArmorStand armorStand) {
        if (frames.isEmpty()) return AnimationController.EMPTY;
        return AnimationPlayer.playAnimation(AnimationPlayer.SYNC, armorStand, plugin, this);
    }

    /**
     * Plays this animation by running an asynchronous task for that plugin that moves
     * the armor stand
     *
     * @param plugin     Plugin to run on
     * @param armorStand Armor stand to animate
     * @return An animation controller for this animation
     * @see #play(Plugin, ArmorStand)
     * @see AnimationController
     */
    public @NotNull AnimationController playAsync(@NotNull Plugin plugin, @NotNull ArmorStand armorStand) {
        if (frames.isEmpty()) return AnimationController.EMPTY;
        return AnimationPlayer.playAnimation(AnimationPlayer.ASYNC, armorStand, plugin, this);
    }

}
