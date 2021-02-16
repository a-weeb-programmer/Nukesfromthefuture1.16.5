package net.nukesfromthefuture.containers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.nukesfromthefuture.main.Nukesfromthefuture;
import net.nukesfromthefuture.tileentity.TileBeta;


public class BetaContainer extends Container {
        public TileBeta te;
    public PlayerEntity player;
    public IItemHandler playerInv;
    public BetaContainer(int id, BlockPos pos, World world, PlayerEntity player, PlayerInventory inv) {
        super(Nukesfromthefuture.beta_container, id);
        te = (TileBeta) world.getTileEntity(pos);
        this.player = player;
        this.playerInv = new InvWrapper(inv);
        if(te != null) {
            te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent((h) -> {
                addSlot(new SlotItemHandler(h, 0, 24, 20));
                addSlot(new SlotItemHandler(h, 1, 79, 61));
                addSlot(new SlotItemHandler(h, 2, 130, 20));
            });
        }
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                this.addSlot(new SlotItemHandler(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int i = 0; i < 9; i++)
        {
            this.addSlot(new SlotItemHandler(playerInv, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(te.getWorld(), te.getPos()), player, Nukesfromthefuture.beta_nuke);
    }
}
