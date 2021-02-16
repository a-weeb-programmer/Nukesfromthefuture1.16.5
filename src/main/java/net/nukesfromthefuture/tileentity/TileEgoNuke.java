package net.nukesfromthefuture.tileentity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.EggItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.nukesfromthefuture.containers.EgoContainer;
import net.nukesfromthefuture.main.Nukesfromthefuture;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEgoNuke extends TileEntity implements ISidedInventory, INamedContainerProvider {
    public ItemStack[] slots;
    private ItemStackHandler itemHandler = createHandler();
    private LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    public TileEgoNuke() {
        super(Nukesfromthefuture.ego_type);
        slots = new ItemStack[3];
    }
    //thank god these methods didn't change
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return TileEntity.INFINITE_EXTENT_AABB;
    }
    private ItemStackHandler createHandler() {
        return new ItemStackHandler(3) {

            @Override
            protected void onContentsChanged(int slot) {
                // To make sure the TE persists when the chunk is saved later we need to
                // mark it dirty every time the item handler changes
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if(slot == 0) {
                    return stack.getItem() == Nukesfromthefuture.ego_ingot;
                } else if(slot == 1){
                    return stack.getItem() == Nukesfromthefuture.lead_ingot;
                } else if(slot == 2){
                    return stack.getItem() == Nukesfromthefuture.POTATO;
                }
                return false;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if (slot == 0 && stack.getItem() != Nukesfromthefuture.ego_ingot) {
                    return stack;
                }  else if(slot == 1 && stack.getItem() != Nukesfromthefuture.lead_ingot){
                    return stack;
                } else if(slot == 2 && stack.getItem() != Nukesfromthefuture.POTATO){
                    return stack;
                }
                return super.insertItem(slot, stack, simulate);
            }
        };
    }
    @Override
    public double getMaxRenderDistanceSquared() {
        return 650D;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return false;
    }

    @Override
    public int getSizeInventory() {
        return slots.length;
    }

    @Override
    public boolean isEmpty() {
        if(slots[getSizeInventory()].getCount() != 0) {
            return false;
        } else{
            return true;
        }
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return slots[index];
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2) {
        if (this.slots[par1] != null)
        {
            ItemStack var3;

            if (this.slots[par1].getCount() <= par2)
            {
                var3 = this.slots[par1];
                this.slots[par1] = null;
                return var3;
            }
            else
            {
                var3 = this.slots[par1].split(par2);

                if (this.slots[par1].getCount() == 0)
                {
                    this.slots[par1] = null;
                }

                return var3;
            }
        }
        else
        {
            return null;
        }
    }
    public boolean isReady(){
        if(itemHandler.getStackInSlot(0).getItem() == Nukesfromthefuture.ego_ingot && itemHandler.getStackInSlot(1).getItem() == Nukesfromthefuture.lead_ingot && itemHandler.getStackInSlot(2).getItem() == Nukesfromthefuture.POTATO)
            return true;
        return false;
    }
    @Override
    public ItemStack removeStackFromSlot(int index) {
        return slots[index];
    }

    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
        this.slots[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.getCount() > this.getInventoryStackLimit())
        {
            par2ItemStack.setCount(this.getInventoryStackLimit());
        }
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return this.world.getTileEntity(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())) != this ? false : player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64;
    }

    @Override
    public void clear() {

    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("ego_nuke");
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory inv, PlayerEntity player) {
        return new EgoContainer(i, pos, world, inv, player);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return handler.cast();
        }
        return super.getCapability(cap, side);
    }
}
