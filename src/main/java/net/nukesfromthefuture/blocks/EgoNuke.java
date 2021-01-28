package net.nukesfromthefuture.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.nukesfromthefuture.containers.EgoContainer;
import net.nukesfromthefuture.entity.EntityEgoBlast;
import net.nukesfromthefuture.explosion.EgoExplosion;
import net.nukesfromthefuture.main.Nukesfromthefuture;
import net.nukesfromthefuture.tileentity.TileEgoNuke;

import javax.annotation.Nullable;


public class EgoNuke extends Block {
    public static int aoc = 0;
    public EgoNuke(Block.Properties prop){
        super(prop);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }


    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEgoNuke();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!worldIn.isRemote) {
            TileEntity te = worldIn.getTileEntity(pos);
            if(te instanceof TileEgoNuke) {
                INamedContainerProvider provider = new INamedContainerProvider() {
                    @Override
                    public ITextComponent getDisplayName() {
                        return new StringTextComponent("ego_nuke");
                    }

                    @Nullable
                    @Override
                    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
                        return new EgoContainer(p_createMenu_1_, pos, worldIn, p_createMenu_2_, p_createMenu_3_);
                    }
                };
                NetworkHooks.openGui((ServerPlayerEntity) player, provider, te.getPos());
            }
            if(!player.isSneaking() && player.getHeldItem(handIn) != null && player.getHeldItem(handIn).getItem() == Nukesfromthefuture.POTATO) {
                worldIn.removeBlock(new BlockPos(pos.getX(), pos.getY(), pos.getZ()), false);
                EgoExplosion tsar = new EgoExplosion((int) pos.getX(), (int) pos.getY(), (int) pos.getZ(), worldIn, (int) ((Nukesfromthefuture.egoStrength.get() + (aoc * aoc)) * 0.8f));
                EntityEgoBlast entity = new EntityEgoBlast(worldIn, pos.getX(), pos.getY(), pos.getZ(), tsar, Nukesfromthefuture.egoStrength.get());
                worldIn.addEntity(entity);
            }
        }
        return ActionResultType.SUCCESS;
    }
}
