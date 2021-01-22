package io.github.revxrsal.ama;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an animation frame, where the armorstand has specific
 * body, head, left arm, right arm, left leg and right leg angles.
 * <p>
 * This class is immutable, hence is thread-safe.
 */
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class AnimationFrame {

    private final EulerAngle body;
    private final EulerAngle head;
    private final EulerAngle leftArm;
    private final EulerAngle rightArm;
    private final EulerAngle leftLeg;
    private final EulerAngle rightLeg;

    public void pose(@NotNull ArmorStand stand) {
        stand.setBodyPose(body);
        stand.setHeadPose(head);
        stand.setLeftArmPose(leftArm);
        stand.setRightArmPose(rightArm);
        stand.setLeftLegPose(leftLeg);
        stand.setRightLegPose(rightLeg);
    }

    @Override public String toString() {
        return "AnimationFrame{" +
                "body=" + string(body) +
                ", head=" + string(head) +
                ", leftArm=" + string(leftArm) +
                ", rightArm=" + string(rightArm) +
                ", leftLeg=" + string(leftLeg) +
                ", rightLeg=" + string(rightLeg) +
                '}';
    }

    private static String string(EulerAngle angle) {
        return String.format("EulerAngle{x=%s, y=%s, z=%s}", angle.getX(), angle.getY(), angle.getZ());
    }
}
