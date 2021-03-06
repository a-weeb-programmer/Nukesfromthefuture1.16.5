package net.nukesfromthefuture.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.nukesfromthefuture.entity.MK3Explosion;
import net.nukesfromthefuture.main.Nukesfromthefuture;
import net.nukesfromthefuture.tileentity.ColliderTile;

import javax.annotation.Nullable;

public class SingularityNuke extends Block {
    public static DirectionProperty facing = HorizontalBlock.HORIZONTAL_FACING;
    public SingularityNuke(Block.Properties prop){
        super(prop);
        this.setDefaultState(this.getStateContainer().getBaseState().with(facing, Direction.NORTH));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ColliderTile();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(facing, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!worldIn.isRemote){
            ItemStack stack = player.getHeldItem(handIn);
            TileEntity te = worldIn.getTileEntity(pos);
            if(te instanceof ColliderTile){
                NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) te, pos);
            }
            if(!player.isSneaking() && stack.getItem() == Nukesfromthefuture.UwU){
                MK3Explosion entity = new MK3Explosion(worldIn, pos.getX(), pos.getY(), pos.getZ(), Nukesfromthefuture.beta_strength.get(), Nukesfromthefuture.beta_speed.get());
                worldIn.addEntity(entity);
                worldIn.removeBlock(pos, false);
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(facing)));
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(facing, rot.rotate(state.get(facing)));
    }
    @Override
    public void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
        builder.add(facing);
    }
}
