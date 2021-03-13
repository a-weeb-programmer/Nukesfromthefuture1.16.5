package net.nukesfromthefuture.main;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.nukesfromthefuture.containers.FluidContainer;
import net.nukesfromthefuture.containers.FluidContainerRegistry;

public class FluidBarrelTypes {
    public static void registerTypesToBarrels(){
        ItemStack stack = new ItemStack(Nukesfromthefuture.fluid_barrel_full);
        CompoundNBT tag = stack.getTag();
        if(tag != null){
            if(tag.getString("type").equals(FluidHandler.FluidType.black_hole_fuel.toString())){
                FluidContainerRegistry.registerContainer(new FluidContainer(stack, new ItemStack(Nukesfromthefuture.fluid_barrel_empty), FluidHandler.FluidType.black_hole_fuel, 16000));
            }
        }
    }
}
