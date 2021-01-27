package net.nukesfromthefuture.tabs;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.nukesfromthefuture.main.Nukesfromthefuture;

public class ResourceTab extends ItemGroup {
    public ResourceTab(String lable){
        super(lable);
    }
    public ItemStack createIcon(){
        return new ItemStack(Nukesfromthefuture.egonium_ore);
    }
}
