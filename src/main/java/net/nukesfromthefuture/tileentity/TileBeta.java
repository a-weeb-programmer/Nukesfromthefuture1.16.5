package net.nukesfromthefuture.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.*;
import net.nukesfromthefuture.containers.BetaContainer;
import net.nukesfromthefuture.main.Nukesfromthefuture;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

public class TileBeta extends TileEntity implements INamedContainerProvider {
    //I'm trying a different approach than i did for the ego nuke tile entity... this better not break stuff
    public IItemHandler uwu = createHandler();
    public LazyOptional<IItemHandler> a = LazyOptional.of(() -> uwu);
    public TileBeta() {
        super(Nukesfromthefuture.beta_type);
    }


    public ItemStackHandler createHandler(){
        return new ItemStackHandler(3){
            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                return super.insertItem(slot, stack, simulate);
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return true;
            }
        };

    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("Beta");
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        return new BetaContainer(p_createMenu_1_, getPos(), world, p_createMenu_3_, p_createMenu_2_);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return a.cast();
        }
        return super.getCapability(cap);
    }

}
