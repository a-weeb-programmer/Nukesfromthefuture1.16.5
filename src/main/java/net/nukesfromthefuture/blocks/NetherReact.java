package net.nukesfromthefuture.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.nukesfromthefuture.tileentity.TileNReactor;

public class NetherReact extends Block {
    public NetherReact(Properties properties) {
        super(properties);
    }
    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        TileNReactor entity = (TileNReactor) world.getTileEntity(pos);
        if (!player.isSneaking()) {
            if (!entity.isValid()) {
                if (world.isRemote) {
                    player.sendStatusMessage(new StringTextComponent("Structure not correctly set up"), false);
                }
            } else if (entity.isValid()) {
                if (world.isRemote) {
                    player.sendStatusMessage(new StringTextComponent(TextFormatting.DARK_RED + "Activated successfully"), false);
                }
                entity.buildSpire(world, pos.getX(), pos.getY(), pos.getZ());
                world.removeBlock(pos, false);
                entity.replaceBlocks(world, pos.getX(), pos.getY(), pos.getZ());
            } else if (entity.Activated() && !entity.isValid()) {
                if (world.isRemote) {
                    player.sendStatusMessage(new StringTextComponent("It has already been activated bruh"), false);
                }
            }
            return ActionResultType.SUCCESS;
        }

        return ActionResultType.SUCCESS;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
    public TileEntity createTileEntity(BlockState state, IBlockReader reader){
        return new TileNReactor();
    }
}
