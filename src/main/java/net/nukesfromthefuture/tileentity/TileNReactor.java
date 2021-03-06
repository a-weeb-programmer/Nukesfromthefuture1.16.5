package net.nukesfromthefuture.tileentity;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.nukesfromthefuture.main.Nukesfromthefuture;

public class TileNReactor extends TileEntity implements ITickableTileEntity {
    public TileNReactor(){
        super(Nukesfromthefuture.reactor);
    }
    public int age = 0;
    public boolean isValid(){
        if(world.getBlockState(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ())) == Blocks.COBBLESTONE.getDefaultState() &&
                world.getBlockState(new BlockPos(pos.getX() + 1, pos.getY() - 1, pos.getZ())) == Blocks.COBBLESTONE.getDefaultState() &&
                world.getBlockState(new BlockPos(pos.getX() - 1, pos.getY() - 1, pos.getZ())) == Blocks.COBBLESTONE.getDefaultState() &&
                world.getBlockState(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ() + 1)) == Blocks.COBBLESTONE.getDefaultState() &&
                world.getBlockState(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ() - 1)) == Blocks.COBBLESTONE.getDefaultState() &&
                world.getBlockState(new BlockPos(pos.getX() + 1, pos.getY() - 1, pos.getZ() + 1)) == Blocks.GOLD_BLOCK.getDefaultState() &&
                world.getBlockState(new BlockPos(pos.getX() + 1, pos.getY() - 1, pos.getZ() - 1)) == Blocks.GOLD_BLOCK.getDefaultState() &&
                world.getBlockState(new BlockPos(pos.getX() - 1, pos.getY() - 1, pos.getZ() + 1)) == Blocks.GOLD_BLOCK.getDefaultState() &&
                world.getBlockState(new BlockPos(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1)) == Blocks.GOLD_BLOCK.getDefaultState() &&
                world.getBlockState(new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ() + 1)) == Blocks.COBBLESTONE.getDefaultState() &&
                world.getBlockState(new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ() - 1)) == Blocks.COBBLESTONE.getDefaultState() &&
                world.getBlockState(new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ() + 1)) == Blocks.COBBLESTONE.getDefaultState() &&
                world.getBlockState(new BlockPos(pos.getX() -1, pos.getY(), pos.getZ() - 1)) == Blocks.COBBLESTONE.getDefaultState() &&
                world.getBlockState(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ())) == Blocks.COBBLESTONE.getDefaultState() &&
                world.getBlockState(new BlockPos(pos.getX() + 1, pos.getY() + 1, pos.getZ())) == Blocks.COBBLESTONE.getDefaultState() &&
                world.getBlockState(new BlockPos(pos.getX() - 1, pos.getY() + 1, pos.getZ())) == Blocks.COBBLESTONE.getDefaultState() &&
                world.getBlockState(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ() + 1)) == Blocks.COBBLESTONE.getDefaultState() &&
                world.getBlockState(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ() - 1)) == Blocks.COBBLESTONE.getDefaultState()) return true;
        return false;
    }

    public void buildSpire(World world, int x, int y, int z) {
        //YAS, I did this all in my head *laughs in no brain cells*
        if(!world.isRemote) {
            for (int j = 0; j < 12; j++) {
                for (int i = 0; i < 12; i++) {
                    world.setBlockState(new BlockPos((x - 5) + i, y - 2, (z - 5) + j), Blocks.NETHERRACK.getDefaultState());
                    world.setBlockState(new BlockPos(x + 6, (y - 2) + i, (z - 5) + j), Blocks.NETHERRACK.getDefaultState());
                    world.setBlockState(new BlockPos(x - 6, (y - 2) + i, (z - 5) + j), Blocks.NETHERRACK.getDefaultState());
                    world.setBlockState(new BlockPos((x - 5) + j, (y - 2) + i, z + 6), Blocks.NETHERRACK.getDefaultState());
                    world.setBlockState(new BlockPos((x - 5) + j, y + 26, z + 6), Blocks.NETHER_BRICK_FENCE.getDefaultState());
                    world.setBlockState(new BlockPos(x + 6, y + 26, (z - 6) + j), Blocks.NETHER_BRICK_FENCE.getDefaultState());
                    world.setBlockState(new BlockPos(x + 6, y + 26, (z + 6) + j), Blocks.NETHER_BRICK_FENCE.getDefaultState());
                    world.setBlockState(new BlockPos((x - 5) + j, (y - 2) + i, z - 6), Blocks.NETHERRACK.getDefaultState());
                    for(int l = 0; l < 13; l++) {
                        for (int k = 0; k < 13; k++) {
                            world.setBlockState(new BlockPos((x - 6) + k, y + 10, (z - 6) + l), Blocks.NETHERRACK.getDefaultState());
                            world.setBlockState(new BlockPos((x - 6) + k,y + 25, (z - 6) + l), Blocks.NETHER_BRICKS.getDefaultState());
                        }
                    }
                    for(int p = 0; p < 14; p++) {
                        for (int n = 0; n < 14; n++) {
                            for (int o = 0; o < 7; o++) {
                                world.setBlockState(new BlockPos((x - 3) + o, (y + 11) + p, z + 3), Blocks.NETHER_BRICKS.getDefaultState());
                                world.setBlockState(new BlockPos(x + 3, (y + 11) + p, (z - 3) + o), Blocks.NETHER_BRICKS.getDefaultState());
                                world.setBlockState(new BlockPos((x - 3) + o, (y + 11) + p, z - 3), Blocks.NETHER_BRICKS.getDefaultState());
                                world.setBlockState(new BlockPos(x - 3, (y + 11) + p, (z - 3) + o), Blocks.NETHER_BRICKS.getDefaultState());
                            }
                        }
                    }
                }
            }
        }

    }
    @Deprecated
    public void spawnEntites(World world){
        if(!world.isRemote) {
            PiglinEntity entity = new PiglinEntity(EntityType.PIGLIN, world);
            entity.setPosition(pos.getX() + 4, pos.getY() - 1, pos.getZ());
            world.addEntity(entity);
            world.addEntity(entity);
            world.addEntity(entity);
            world.addEntity(entity);
            world.addEntity(entity);


        }
    }
    @OnlyIn(Dist.CLIENT)
    public void replaceBlocks(World world, int x, int y, int z){
        world.setBlockState(new BlockPos(x, y - 1, z), Nukesfromthefuture.red_obsidian.getDefaultState());
        world.setBlockState(new BlockPos(x + 1, y - 1, z), Nukesfromthefuture.red_obsidian.getDefaultState());
        world.setBlockState(new BlockPos(x - 1, y - 1, z), Nukesfromthefuture.red_obsidian.getDefaultState());
        world.setBlockState(new BlockPos(x, y - 1, z + 1), Nukesfromthefuture.red_obsidian.getDefaultState());
        world.setBlockState(new BlockPos(x, y - 1, z - 1), Nukesfromthefuture.red_obsidian.getDefaultState());
        world.setBlockState(new BlockPos(x + 1, y, z + 1), Nukesfromthefuture.red_obsidian.getDefaultState());
        world.setBlockState(new BlockPos(x + 1, y, z - 1), Nukesfromthefuture.red_obsidian.getDefaultState());
        world.setBlockState(new BlockPos(x - 1, y, z + 1), Nukesfromthefuture.red_obsidian.getDefaultState());
        world.setBlockState(new BlockPos(x - 1, y, z - 1), Nukesfromthefuture.red_obsidian.getDefaultState());
        world.setBlockState(new BlockPos(x, y + 1, z), Nukesfromthefuture.red_obsidian.getDefaultState());
        world.setBlockState(new BlockPos(x + 1, y + 1, z), Nukesfromthefuture.red_obsidian.getDefaultState());
        world.setBlockState(new BlockPos(x - 1, y + 1, z), Nukesfromthefuture.red_obsidian.getDefaultState());
        world.setBlockState(new BlockPos(x, y + 1, z + 1), Nukesfromthefuture.red_obsidian.getDefaultState());
        world.setBlockState(new BlockPos(x, y + 1, z - 1), Nukesfromthefuture.red_obsidian.getDefaultState());
        world.removeBlock(new BlockPos(x, y, z), false);
        world.setBlockState(new BlockPos(x, y, z), Nukesfromthefuture.nether_reactor_2.getDefaultState());

    }
    public boolean Activated(){
        if(world.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ())) == Nukesfromthefuture.nether_reactor_2.getDefaultState()) return true;
        return false;
    }

    @Override
    public void tick() {

        if(this.Activated()) {
            age ++;
            if (age == 1000) {
                world.setBlockState(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ()), Blocks.OBSIDIAN.getDefaultState());
                world.setBlockState(new BlockPos(pos.getX() + 1, pos.getY() - 1, pos.getZ()), Blocks.OBSIDIAN.getDefaultState());
                world.setBlockState(new BlockPos(pos.getX() - 1, pos.getY() - 1, pos.getZ()), Blocks.OBSIDIAN.getDefaultState());
                world.setBlockState(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ() + 1), Blocks.OBSIDIAN.getDefaultState());
                world.setBlockState(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ() - 1), Blocks.OBSIDIAN.getDefaultState());
                world.setBlockState(new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ() + 1), Blocks.OBSIDIAN.getDefaultState());
                world.setBlockState(new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ() - 1), Blocks.OBSIDIAN.getDefaultState());
                world.setBlockState(new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ() + 1), Blocks.OBSIDIAN.getDefaultState());
                world.setBlockState(new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ() - 1), Blocks.OBSIDIAN.getDefaultState());
                world.setBlockState(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()), Blocks.OBSIDIAN.getDefaultState());
                world.setBlockState(new BlockPos(pos.getX() + 1, pos.getY() + 1, pos.getZ()), Blocks.OBSIDIAN.getDefaultState());
                world.setBlockState(new BlockPos(pos.getX() - 1, pos.getY() + 1, pos.getZ()), Blocks.OBSIDIAN.getDefaultState());
                world.setBlockState(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ() + 1), Blocks.OBSIDIAN.getDefaultState());
                world.setBlockState(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ() - 1), Blocks.OBSIDIAN.getDefaultState());

            }

            if(age == 200){
                world.setBlockState(new BlockPos(pos.getX() + 1, pos.getY() - 1, pos.getZ() + 1), Nukesfromthefuture.red_obsidian.getDefaultState());
                world.setBlockState(new BlockPos(pos.getX() + 1, pos.getY() - 1, pos.getZ() - 1), Nukesfromthefuture.red_obsidian.getDefaultState());
                world.setBlockState(new BlockPos(pos.getX() - 1, pos.getY() - 1, pos.getZ() + 1), Nukesfromthefuture.red_obsidian.getDefaultState());
                world.setBlockState(new BlockPos(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1), Nukesfromthefuture.red_obsidian.getDefaultState());
            }
            if(age == 1100){
                world.setBlockState(new BlockPos(pos.getX() + 1, pos.getY() - 1, pos.getZ() + 1), Blocks.OBSIDIAN.getDefaultState());
                world.setBlockState(new BlockPos(pos.getX() + 1, pos.getY() - 1, pos.getZ() - 1), Blocks.OBSIDIAN.getDefaultState());
                world.setBlockState(new BlockPos(pos.getX() - 1, pos.getY() - 1, pos.getZ() + 1), Blocks.OBSIDIAN.getDefaultState());
                world.setBlockState(new BlockPos(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1), Blocks.OBSIDIAN.getDefaultState());
                world.setBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ()), Nukesfromthefuture.nether_reactor_burned_out.getDefaultState());
            }
            if(!world.isRemote) {
                if (age == 200) {
                    PiglinEntity entity = new PiglinEntity(EntityType.PIGLIN, world);
                    entity.setPosition(pos.getX() + 4, pos.getY() - 1, pos.getZ());
                    world.addEntity(entity);

                }
                if (age == 214) {
                    PiglinEntity entity = new PiglinEntity(EntityType.PIGLIN, world);
                    entity.setPosition(pos.getX() + 4, pos.getY() - 1, pos.getZ() - 3);
                    world.addEntity(entity);
                }
                if (age == 50 || age == 100 || age == 150) {
                    PiglinEntity entity = new PiglinEntity(EntityType.PIGLIN, world);
                    entity.setPosition(pos.getX() - 4, pos.getY() - 1, pos.getZ() + 3);
                    world.addEntity(entity);
                }
                if (age == 20) {
                    for (int i = 0; i < 11; i++) {
                        for (int j = 0; j < 11; j++) {
                            ItemEntity sthuff = new ItemEntity(world, (pos.getX() - 5) + i, pos.getY() - 1, (pos.getZ() - 5) + j, new ItemStack(Items.BLAZE_ROD));
                            ItemEntity more = new ItemEntity(world, (pos.getX() - 5) + i, pos.getY() - 1, (pos.getZ() - 5) + j, new ItemStack(Items.MELON));
                            ItemEntity egos = new ItemEntity(world, (pos.getX() - 5) + i, pos.getY() - 1, (pos.getZ() - 5) + j, new ItemStack(Nukesfromthefuture.UwU));
                            ItemEntity test = new ItemEntity(world, (pos.getX() - 5) + i, pos.getY() - 1, (pos.getZ() - 5) + j, new ItemStack(Items.NETHER_STAR));
                            world.addEntity(test);
                            world.addEntity(sthuff);
                            world.addEntity(more);
                            world.addEntity(egos);

                        }

                    }
                }
            }




        }
    }

}
