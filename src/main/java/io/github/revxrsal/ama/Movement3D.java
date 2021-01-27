package io.github.revxrsal.ama;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

/**
 * Represents movement on third dimensional on the Cartesian plane.
 */
@Getter
@AllArgsConstructor
public class Movement3D {

    private final double deltaX, deltaY, deltaZ;

    public Location add(@NotNull Location location) {
        return location.add(deltaX, deltaY, deltaZ);
    }

}
