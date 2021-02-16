package net.nukesfromthefuture.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.*;
import net.minecraft.util.text.filter.ChatFilterClient;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class Donut extends Item {
    public Donut(Properties prop){
        super(prop);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new StringTextComponent("hmmm, yes, the static tastes like static"));
        tooltip.add(new StringTextComponent("                     "));
        tooltip.add(new StringTextComponent(TextFormatting.DARK_RED + "starts speaking 90's cable tv when antenna is misaligned"));

    }
}
