package net.nukesfromthefuture.items;

import com.google.common.collect.Maps;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.nukesfromthefuture.containers.FluidTank;
import net.nukesfromthefuture.main.FluidHandler.FluidType;
import net.nukesfromthefuture.main.Nukesfromthefuture;

import java.util.List;
import java.util.Map;

public class FluidTankItem extends Item{
    public static Map<FluidType, FluidTankItem> tanks = Maps.newIdentityHashMap();
    public FluidType type;
    public String name;
    public FluidTankItem(Item.Properties prop) {
        super(prop);
    }
    public ITextComponent getDisplayName(ItemStack stack)
    {

        String s = ("" + I18n.format(this.getRegistryName() + ".name").trim());
        String s1 = ("" + I18n.format(FluidType.getEnumFromName(stack.getTag().getString("type")).getUnlocalizedName()).trim());

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
            int j = FluidType.getEnumFromName(stack.getTag().getString("type")).getMSAColor();

            if (j < 0)
            {
                j = 16777215;
            }

            return j;
        }
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> list) {
        if (group == Nukesfromthefuture.machines || group == ItemGroup.SEARCH) {
            for (int i = 1; i < FluidType.values().length; ++i) {
                ItemStack stack = new ItemStack(this, 1);
                stack.setTag(new CompoundNBT());
                stack.getTag().putString("type", FluidType.getEnum(i).getName());
                list.add(stack);
            }
        }
    }
}
