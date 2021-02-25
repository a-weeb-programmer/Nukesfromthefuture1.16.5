package net.nukesfromthefuture.containers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.nukesfromthefuture.main.Nukesfromthefuture;
import net.nukesfromthefuture.tileentity.ColliderTile;

import javax.annotation.Nullable;

public class ColliderContainer extends Container {
    public IItemHandler real_inv;
    public ColliderTile te;
    public PlayerEntity player;
    public ColliderContainer(int id, BlockPos pos, World world, PlayerEntity player, PlayerInventory inv) {
        super(Nukesfromthefuture.collider_container, id);
        te = (ColliderTile) world.getTileEntity(pos);
        real_inv = new InvWrapper(inv);
        this.player = player;
        if(te != null){
            te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent((h) -> {
                addSlot(new SlotItemHandler(h, 0, 35, 18));
                addSlot(new SlotItemHandler(h, 1, 35, 58));
                addSlot(new SlotItemHandler(h, 2, 77, 37));
                addSlot(new SlotItemHandler(h, 3, 119, 18));
                addSlot(new SlotItemHandler(h, 4, 119, 58));
                this.addSlot(new SlotItemHandler(h, 5, 147, 58));
                this.addSlot(new SlotItemHandler(h, 6, 7, 30));
            });
        }
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 9; j++){
                addSlot(new SlotItemHandler(real_inv, j + i * 9 + 9, 6 + j * 18, 83 + i * 18));
            }
        }
        for(int i = 0; i < 9; i++){
            addSlot(new SlotItemHandler(real_inv, i, 6 + i * 18, 141));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(te.getWorld(), te.getPos()), player, Nukesfromthefuture.singularity_nuke);
    }
}
