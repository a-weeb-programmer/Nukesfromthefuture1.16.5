package net.nukesfromthefuture.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EggItem;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.nukesfromthefuture.entity.POTATOEntity;
import net.nukesfromthefuture.main.Nukesfromthefuture;

import javax.annotation.Nullable;
import java.util.List;

public class POTATO extends Item {
    public POTATO(Item.Properties prop){
        super(prop);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        worldIn.playSound((PlayerEntity)null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
        if(!worldIn.isRemote){
            POTATOEntity entity = new POTATOEntity(worldIn, playerIn);
            entity.setItem(stack);
            entity.func_234612_a_(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
            worldIn.addEntity(entity);
        }
        return ActionResult.func_233538_a_(stack, worldIn.isRemote());
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        return new StringTextComponent(TextFormatting.GOLD + "POTATO");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new StringTextComponent("WORSHIP LE POTATO!!!"));
        tooltip.add(new StringTextComponent("THE LEGENDARY POTATO"));
    }
}
