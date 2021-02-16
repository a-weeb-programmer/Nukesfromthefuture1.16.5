package net.nukesfromthefuture.items;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ColorHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.nukesfromthefuture.main.FluidHandler;

import javax.annotation.Nullable;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class ItemFluidIdentidier extends Item {
    //DO NOT CALL THIS VARIABLE OUTSIDE THE CLASS
    public FluidHandler.FluidType type;
    public static Map<FluidHandler.FluidType, ItemFluidIdentidier> a = Maps.newIdentityHashMap();
    public ItemFluidIdentidier(Item.Properties prop, FluidHandler.FluidType type)
    {
        super((prop));
        this.type = type;
        a.put(type, this);
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
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, list, flagIn);
        if (!(stack.getItem() instanceof ItemFluidIdentidier))
            return;

        list.add(new StringTextComponent("[CREATED USING TEMPLATE FOLDER]"));
        list.add(new StringTextComponent(""));
        list.add(new StringTextComponent("Universal fluid identifier for:"));
        list.add(new StringTextComponent("   " + I18n.format(type.getUnlocalizedName())));
    }
    public FluidHandler.FluidType getType(ItemStack stack) {
        return type;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        TileEntity te = context.getWorld().getTileEntity(context.getPos());
        return ActionResultType.SUCCESS;
    }

    @OnlyIn(Dist.CLIENT)
    public int getColor(int tint, ItemStack stack)
    {
        if (tint == 0)
        {
            return 16777215;
        }
        else
        {
            return type.getMSAColor();
        }
    }
    public static Iterable<ItemFluidIdentidier> getIdentifier() {
        return Iterables.unmodifiableIterable(a.values());
    }

}
