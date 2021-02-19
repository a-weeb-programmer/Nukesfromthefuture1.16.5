package net.nukesfromthefuture.containers;

import net.minecraft.item.ItemStack;
import net.nukesfromthefuture.main.FluidHandler.FluidType;

import java.util.ArrayList;
import java.util.List;
/**The use of this class file is now depreciated. Fluid containers should be registered with the IFluidTankItem
 * it won't work if a fluid container is registered with this*/
@Deprecated
public class FluidContainerRegistry {
    public static List<FluidContainer> allContainers = new ArrayList<FluidContainer>();
    @Deprecated
    public static void registerContainer(FluidContainer con) {
        allContainers.add(con);
    }
    @Deprecated
    public static int getFluidContent(ItemStack stack, FluidType type) {

        if(stack == null)
            return 0;

        ItemStack sta = stack.copy();
        sta.setCount(1);

        for(FluidContainer container : allContainers) {
            if(container.type.name().equals(type.name()) &&
                    ItemStack.areItemStacksEqual(container.fullContainer, sta) &&
                    ItemStack.areItemStackTagsEqual(container.fullContainer, sta))
                return container.content;
        }

        return 0;
    }
    @Deprecated
    public static FluidType getFluidType(ItemStack stack) {

        if(stack == null)
            return FluidType.None;

        ItemStack sta = stack.copy();
        sta.setCount(1);

        for(FluidContainer container : allContainers) {
            if(ItemStack.areItemStacksEqual(container.fullContainer, sta) &&
                    ItemStack.areItemStackTagsEqual(container.fullContainer, sta))
                return container.type;
        }

        return FluidType.None;
    }
    @Deprecated
    public static ItemStack getFullContainer(ItemStack stack, FluidType type) {
        if(stack == null)
            return null;

        ItemStack sta = stack.copy();
        sta.setCount(1);

        for(FluidContainer container : allContainers) {
            if(ItemStack.areItemStacksEqual(container.emptyContainer, sta) &&
                    ItemStack.areItemStackTagsEqual(container.emptyContainer, sta) &&
                    container.type.name().equals(type.name()))
                return container.fullContainer.copy();
        }

        return null;
    }
    @Deprecated
    public static ItemStack getEmptyContainer(ItemStack stack) {
        if(stack == null)
            return null;

        ItemStack sta = stack.copy();
        sta.setCount(1);

        for(FluidContainer container : allContainers) {
            if(ItemStack.areItemStacksEqual(container.fullContainer, sta) &&
                    ItemStack.areItemStackTagsEqual(container.fullContainer, sta))
                return container.emptyContainer.copy();
        }

        return null;
    }

}
