package net.bird.projectcataclysm.block.custom;

import net.minecraft.util.StringIdentifiable;

public enum LaunchPlatformType implements StringIdentifiable {
    CONTROL_PANEL,
    BASE,
    STRAIGHT_STAIRS,
    OUTER_STAIRS;

    private LaunchPlatformType() {
    }

    public String toString() { return this.asString(); }

    public String asString() {
        return this == CONTROL_PANEL ? "control_panel" : this == BASE ? "base" : this == STRAIGHT_STAIRS ? "straight_stairs" : "outer_stairs";
    }
}
