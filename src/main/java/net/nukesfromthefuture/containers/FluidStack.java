package net.nukesfromthefuture.containers;

import net.nukesfromthefuture.main.FluidHandler.FluidType;

public class FluidStack {
    public int fill;
    public FluidType type;

    public FluidStack(int fill, FluidType type) {
        this.fill = fill;
        this.type = type;
    }
}
