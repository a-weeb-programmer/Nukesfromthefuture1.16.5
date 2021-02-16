package net.nukesfromthefuture.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.nukesfromthefuture.containers.BetaContainer;
import net.nukesfromthefuture.containers.EgoContainer;
import net.nukesfromthefuture.entity.Blast;
import net.nukesfromthefuture.entity.MK3Explosion;
import net.nukesfromthefuture.main.Nukesfromthefuture;
import net.nukesfromthefuture.tileentity.TileBeta;
import net.nukesfromthefuture.tileentity.TileEgoNuke;

import javax.annotation.Nullable;


public class Beta extends Block {
    public Beta(Properties prop){
        super(prop);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileBeta();
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!worldIn.isRemote) {
            TileEntity te = worldIn.getTileEntity(pos);
            ItemStack stack = player.getHeldItem(handIn);
            if(te instanceof TileBeta) {
                INamedContainerProvider provider = new INamedContainerProvider() {
                    @Override
                    public ITextComponent getDisplayName() {
                        return new StringTextComponent("beta");
                    }

                    @Nullable
                    @Override
                    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
                        return new BetaContainer(p_createMenu_1_, pos, worldIn, p_createMenu_3_, p_createMenu_2_);
                    }
                };
                NetworkHooks.openGui((ServerPlayerEntity) player, provider, te.getPos());
            }
            if (!player.isSneaking() && stack.getItem() == Nukesfromthefuture.UwU ) {
                MK3Explosion entity = new MK3Explosion(worldIn, pos.getX(), pos.getY(), pos.getZ(), Nukesfromthefuture.beta_strength.get(), Nukesfromthefuture.beta_speed.get());
                worldIn.addEntity(entity);
                worldIn.removeBlock(pos, false);
            }


        }
        return ActionResultType.SUCCESS;
    }
}
