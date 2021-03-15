package net.nukesfromthefuture.items;

import net.minecraft.entity.LivingEntity;
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
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if(!worldIn.isRemote && entityLiving instanceof PlayerEntity){
            ((PlayerEntity)entityLiving).attackEntityFrom(NffDamageSource.taco, 1);
            MK3Explosion entity = new MK3Explosion(worldIn,(int) ((PlayerEntity)entityLiving).getPosX(), (int)((PlayerEntity)entityLiving).getPosY(),(int) ((PlayerEntity)entityLiving).getPosZ(), 90, 14);
            worldIn.addEntity(entity);
        }
        return stack;
    }
}
