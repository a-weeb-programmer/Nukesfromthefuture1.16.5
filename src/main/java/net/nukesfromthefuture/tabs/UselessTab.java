package net.nukesfromthefuture.tabs;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.nukesfromthefuture.main.Nukesfromthefuture;

/**yes I still call them creative tabs. got a problem with that f0rge?*/
public class UselessTab extends ItemGroup {
    public UselessTab(String lable){
        super(lable);
        setBackgroundImageName("uwu.png");
    }
    @Override
    public ItemStack createIcon() {
        return new ItemStack(Nukesfromthefuture.iTrol);
    }
}
