package net.nukesfromthefuture.tabs;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.nukesfromthefuture.main.Nukesfromthefuture;

public class FoodTab extends ItemGroup {
    public FoodTab(String label) {
        super(label);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(Nukesfromthefuture.cooked_POTATO);
    }
}
