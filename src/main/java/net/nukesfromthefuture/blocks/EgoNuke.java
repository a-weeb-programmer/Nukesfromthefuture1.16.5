package net.nukesfromthefuture.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.nukesfromthefuture.tileentity.TileEgoNuke;

import javax.annotation.Nullable;


public class EgoNuke extends Block {
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
}
