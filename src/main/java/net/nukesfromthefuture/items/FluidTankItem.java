package net.nukesfromthefuture.items;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.criterion.TickTrigger;
import net.minecraft.block.LeavesBlock;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.*;
import net.minecraft.util.datafix.fixes.LeavesFix;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.nukesfromthefuture.main.FluidHandler;
import net.nukesfromthefuture.main.FluidHandler.FluidType;

import java.util.List;
import java.util.Map;

public class FluidTankItem extends Item {
    public static Map<FluidType, FluidTankItem> tanks = Maps.newIdentityHashMap();
    public FluidType type;
    public FluidTankItem(FluidType type, Item.Properties prop) {
        super(prop);
        this.type = type;
        tanks.put(type, this);
    }
    public ITextComponent getDisplayName(ItemStack stack)
    {

        String s = ("" + I18n.format(this.getRegistryName() + ".name").trim());
        String s1 = ("" + I18n.format(type.getUnlocalizedName())).trim();

        if (s1 != null)
        {
            s = (s + " " + s1);
        }

        return new StringTextComponent(s);
    }



    @OnlyIn(Dist.CLIENT)
    public int setColor(ItemStack stack, int tint)
    {
        if (tint == 0)
        {
            return 16777215;
        }
        else
        {
            int j = type.getMSAColor();

            if (j < 0)
            {
                j = 16777215;
            }

            return j;
        }
    }
    public static Iterable<FluidTankItem> getTanks(){
        return Iterables.unmodifiableIterable(tanks.values());
    }
}
