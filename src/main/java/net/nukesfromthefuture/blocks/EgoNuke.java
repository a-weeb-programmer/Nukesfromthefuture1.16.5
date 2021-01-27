package net.nukesfromthefuture.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
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
        if(worldIn.isRemote){
            return ActionResultType.CONSUME;
        }
        worldIn.removeBlock(new BlockPos(pos.getX(), pos.getY(), pos.getZ()), false);
        EgoExplosion tsar = new EgoExplosion((int)pos.getX(), (int)pos.getY(), (int)pos.getZ(), worldIn, (int) ((Nukesfromthefuture.egoStrength.get() + (aoc * aoc)) * 0.8f));
        EntityEgoBlast entity = new EntityEgoBlast(worldIn, pos.getX(), pos.getY(), pos.getZ(), tsar, Nukesfromthefuture.egoStrength.get());
        worldIn.addEntity(entity);
        return ActionResultType.SUCCESS;
    }
}
