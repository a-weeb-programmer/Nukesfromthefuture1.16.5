package net.nukesfromthefuture.tabs;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.nukesfromthefuture.main.Nukesfromthefuture;

public class MachineTab extends ItemGroup {
    public MachineTab(String lable){
        super(lable);
    }
    public ItemStack createIcon(){
        return new ItemStack(Nukesfromthefuture.fluid_barrel_empty);
    }
}
