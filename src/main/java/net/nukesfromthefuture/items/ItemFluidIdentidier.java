package net.nukesfromthefuture.items;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import net.minecraft.advancements.criterion.BeeNestDestroyedTrigger;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ColorHelper;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.nukesfromthefuture.main.FluidHandler.*;
import net.nukesfromthefuture.main.Nukesfromthefuture;

import javax.annotation.Nullable;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class ItemFluidIdentidier extends Item {

    public ItemFluidIdentidier(Properties prop)
    {
        super(prop);
    }

    /*@Override
	public String getUnlocalizedName(ItemStack stack)
    {
        int i = stack.getItemDamage();
        return super.getUnlocalizedName() + "." + FluidType.getEnum(i).getName();
    }*/

    public ItemStack getContainerItem(ItemStack stack) {
        return stack.copy();
    }

    public boolean hasContainerItem() {
        return true;
    }

    public boolean doesContainerItemLeaveCraftingGrid(ItemStack stack)
    {
        return false;
    }
    @Override
    public void fillItemGroup(ItemGroup tabs, NonNullList<ItemStack> list)
    {
        if(tabs == Nukesfromthefuture.resources || tabs == ItemGroup.SEARCH)
        for (int i = 0; i < FluidType.values().length; ++i)
        {
            ItemStack stack = new ItemStack(this, 1);
            stack.setTag(new CompoundNBT());
            stack.getTag().putString("type", FluidType.getEnum(i).getName());
            list.add(stack);
        }
    }
    @Override
    public void addInformation(ItemStack stack, World world, List<ITextComponent> list, ITooltipFlag flag)
    {

        if(!(stack.getItem() instanceof ItemFluidIdentidier))
            return;

        list.add(new StringTextComponent("[CREATED USING TEMPLATE FOLDER]"));
        list.add(new StringTextComponent(""));
        list.add(new StringTextComponent("Universal fluid identifier for:"));
        list.add(new StringTextComponent("   " + I18n.format(FluidType.getEnumFromName(stack.getTag().getString("type")).getUnlocalizedName())));
    }

    public static FluidType getType(ItemStack stack) {
        if(stack != null && stack.getItem() instanceof ItemFluidIdentidier)
            return FluidType.getEnumFromName(stack.getTag().getString("type"));
        else
            return FluidType.None;
    }


    @OnlyIn(Dist.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int p_82790_2_)
    {
        if (p_82790_2_ == 0)
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

}
