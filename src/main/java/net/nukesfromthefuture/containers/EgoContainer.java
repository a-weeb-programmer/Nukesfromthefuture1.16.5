package net.nukesfromthefuture.containers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.nukesfromthefuture.main.Nukesfromthefuture;
import net.nukesfromthefuture.tileentity.TileEgoNuke;

import javax.annotation.Nullable;

public class EgoContainer extends Container {
    public TileEntity tileEntity;
    public PlayerEntity player;
    public IItemHandler playerInv;
    public EgoContainer(int id, BlockPos pos, World world, PlayerInventory inv, PlayerEntity player) {
        super(Nukesfromthefuture.ego_container, id);
        tileEntity = world.getTileEntity(pos);
        this.player = player;
        this.playerInv = new InvWrapper(inv);
        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 41, 40));
                addSlot(new SlotItemHandler(h, 1, 77, 40));
                addSlot(new SlotItemHandler(h, 2, 117, 40));
            });

        }
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 9; j++){
                addSlot(new SlotItemHandler(playerInv, j + i * 9 + 9, 9 + j * 18, 93 + i * 18));
            }
        }
        for(int i = 0; i < 9; i++){
            addSlot(new SlotItemHandler(playerInv, i, 9 + i * 18, 151));
        }
    }


    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), player, Nukesfromthefuture.ego_nuke);
    }
}
