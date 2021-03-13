package net.nukesfromthefuture.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Foods;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.nukesfromthefuture.entity.MK3Explosion;
import net.nukesfromthefuture.main.NffDamageSource;

public class NukeTaco extends Item {
    public NukeTaco(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if(!worldIn.isRemote){
            playerIn.attackEntityFrom(NffDamageSource.taco, 1);
            MK3Explosion entity = new MK3Explosion(worldIn,(int) playerIn.getPosX(), (int)playerIn.getPosY(),(int) playerIn.getPosZ(), 90, 14);
            worldIn.addEntity(entity);
        }
        return ActionResult.func_233538_a_(stack, worldIn.isRemote());
    }
}
