package io.github.revxrsal.ama;

import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a controller for an animation that was started.
 * <p>
 * Every animation gets its own {@link AnimationController} when started.
 *
 * @see ArmorStandAnimation#play(Plugin, ArmorStand)
 * @see ArmorStandAnimation#playAsync(Plugin, ArmorStand)
 */
public interface AnimationController {

    /**
     * Pauses the animation.
     */
    void pause();

    /**
     * Cancels the animation. After being cancelled, the animation can no
     * longer be resumed, paused, or have its position changed.
     */
    void cancel();

    /**
     * Resumes the animation. If the animation was not paused, this method will
     * have no effect.
     *
     * @throws IllegalStateException if the animation was cancelled.
     */
    void resume();

    /**
     * Returns whether is the animation paused right now or not.
     * <p>
     * Note that this method does not take into consideration the result
     * of {@link #isCancelled()}.
     *
     * @return Whether is the animation paused right now
     */
    boolean isPaused();

    /**
     * Returns whether is the animation currently running or not.
     * <p>
     * This is the same as evaluating <code>!isPaused() && !isCancelled()</code>
     *
     * @return Whether is the animation running right now or not.
     */
    boolean isRunning();

    /**
     * Returns whether this animation was cancelled or not
     *
     * @return Whether is the animation cancelled or not
     */
    boolean isCancelled();

    /**
     * Returns the current position of the animation
     *
     * @return The current position
     */
    int getCurrentTick();

    /**
     * Sets the current position of the animation.
     *
     * @param tick The new position
     * @throws IllegalArgumentException if the tick is greater than 19 or less than 0
     */
    void setCurrentTick(int tick);

    /**
     * Cancels this animation, and starts a new one with a new
     * animation controller.
     *
     * @return The new animation controller for the new animation.
     */
    @NotNull AnimationController restart();

    /**
     * Represents an "empty" animation controller. Used when
     * playing animations that do not have any frames.
     */// @formatter:off
    AnimationController EMPTY = new AnimationController() {
        @Override public void pause() {}
        @Override public void cancel() {}
        @Override public void resume() {}
        @Override public boolean isPaused() { return false; }
        @Override public boolean isRunning() { return false; }
        @Override public boolean isCancelled() { return true; }
        @Override public int getCurrentTick() { return 0; }
        @Override public void setCurrentTick(int tick) {}
        @Override public @NotNull AnimationController restart() { return EMPTY; }
    };// @formatter:on

}
