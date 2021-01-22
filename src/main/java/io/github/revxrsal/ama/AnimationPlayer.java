package io.github.revxrsal.ama;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A class for handling playing animations
 */
final class AnimationPlayer {

    static final PlayingFunction SYNC = BukkitRunnable::runTaskTimer;
    static final PlayingFunction ASYNC = BukkitRunnable::runTaskTimerAsynchronously;

    private static final int STARTS_AT = 0;
    private static final int MAX_LENGTH = 19;
    private static final int PAUSED = -1;

    static AnimationController playAnimation(PlayingFunction playingFunction, @NotNull ArmorStand stand, @NotNull Plugin plugin, @NotNull ArmorStandAnimation animation) {
        Objects.requireNonNull(animation, "animation is null!");
        Objects.requireNonNull(stand, "stand is null!");
        AtomicInteger timer = new AtomicInteger(STARTS_AT);
        AtomicInteger pausedAt = new AtomicInteger();
        if (animation.isLooping()) {
            BukkitTask task = playingFunction.start(new BukkitRunnable() {
                @Override public void run() {
                    if (timer.get() == PAUSED) return;
                    if (stand.isDead()) {
                        cancel();
                        return;
                    }
                    int next = timer.getAndIncrement();
                    if (next >= MAX_LENGTH) {
                        timer.set(next = STARTS_AT);
                    }
                    AnimationFrame frame = animation.getFrames().get(next);
                    if (frame != null) frame.pose(stand);
                }
            }, plugin, 1, 1);
            return new AnimationController() {
                @Override public void pause() {
                    pausedAt.set(timer.getAndSet(PAUSED));
                }

                @Override public void cancel() {
                    task.cancel();
                }

                @Override public void resume() {
                    if (!isPaused()) return;
                    timer.set(pausedAt.get());
                }

                @Override public boolean isPaused() {
                    return timer.get() == PAUSED;
                }

                @Override public boolean isRunning() {
                    return Bukkit.getScheduler().isCurrentlyRunning(task.getTaskId()) && !isPaused();
                }

                @Override public boolean isCancelled() {
                    return !Bukkit.getScheduler().isCurrentlyRunning(task.getTaskId());
                }

                @Override public int getCurrentTick() {
                    return timer.get();
                }

                @Override public void setCurrentTick(int tick) {
                    Preconditions.checkArgument(tick < MAX_LENGTH && tick > PAUSED, "Invalid position: " + tick);
                    timer.set(tick);
                }

                @Override public @NotNull AnimationController restart() {
                    cancel();
                    return playAnimation(playingFunction, stand, plugin, animation);
                }
            };
        } else {
            BukkitRunnable br = new BukkitRunnable() {
                @Override public void run() {
                    if (timer.get() == PAUSED) return;
                    int next = timer.getAndIncrement();
                    if (stand.isDead() || next >= MAX_LENGTH) {
                        cancel();
                        return;
                    }
                    AnimationFrame frame = animation.getFrames().get(next);
                    if (frame != null) frame.pose(stand);
                }
            };
            BukkitTask task = playingFunction.start(br, plugin, 1, 1);
            return new AnimationController() {
                @Override public void pause() {
                    pausedAt.set(timer.getAndSet(PAUSED));
                }

                @Override public void cancel() {
                    task.cancel();
                }

                @Override public void resume() {
                    if (!isPaused()) return;
                    timer.set(pausedAt.get());
                }

                @Override public boolean isPaused() {
                    return timer.get() == PAUSED;
                }

                @Override public boolean isRunning() {
                    return !isCancelled() && !isPaused();
                }

                @Override public boolean isCancelled() {
                    return !Bukkit.getScheduler().isCurrentlyRunning(task.getTaskId());
                }

                @Override public int getCurrentTick() {
                    return timer.get();
                }

                @Override public void setCurrentTick(int tick) {
                    Preconditions.checkArgument(tick < MAX_LENGTH && tick > PAUSED, "Invalid position: " + tick);
                    timer.set(tick);
                }

                @Override public @NotNull AnimationController restart() {
                    cancel();
                    return playAnimation(playingFunction, stand, plugin, animation);
                }
            };
        }
    }

    public interface PlayingFunction {

        BukkitTask start(BukkitRunnable bukkitRunnable, Plugin plugin, long a, long b);
    }

}
