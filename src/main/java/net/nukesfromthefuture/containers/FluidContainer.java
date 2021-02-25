package net.nukesfromthefuture.containers;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.nukesfromthefuture.interfaces.IFluidTankItem;
import net.nukesfromthefuture.main.FluidHandler;

public class FluidContainer {
    public ItemStack fullContainer;
    //Them empty container (e.g. empty cell)
    public ItemStack emptyContainer;
    //The type of the contained liquid (e.g. deuterium)
    public FluidHandler.FluidType type;
    //The amount of liquid stored in mB (e.g. 1000)
    public int content;

    public FluidContainer(ItemStack full, ItemStack empty, FluidHandler.FluidType type, int amount) {
        fullContainer = full;
        emptyContainer = empty;
        this.type = type;
        content = amount;
    }

}
