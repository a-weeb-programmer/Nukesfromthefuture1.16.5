package net.nukesfromthefuture.explosion;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.nukesfromthefuture.entity.EntityEgoBlast;
import net.nukesfromthefuture.main.Nukesfromthefuture;
import org.lwjgl.system.CallbackI;

public class EgoExplosion {
    public int		posX;
    public int		posY;
    public int		posZ;
    public int		lastposX = 0;
    public int		lastposZ = 0;
    public int		radius;
    public World	worldObj;
    private int		n = 1;
    private int		nlimit;
    private int		shell;
    private int		leg;
    private int		element;
    private int		repeatCount	= 0;
    private boolean isTree;
    private int 	treeHeight;
    public int processedchunks = 0;

    public EgoExplosion(int x, int y, int z, World world, int rad)
    {
        posX = x;
        posY = y;
        posZ = z;
        worldObj = world;
        radius = rad;
        //int radiussmaller = (radius >> 2) + 45;
        //if (radiussmaller < radius) radius = radiussmaller;
        nlimit = ((radius + 25) * (radius + 25)) * 4;
        rad = rad*rad;
        if (world.isRemote) return;
        System.out.println("radius:" + radius);
        System.out.println("Nlimit:" + nlimit);
        int clamprad = radius;
        //if (clamprad > 50) clamprad = 50;
        for (int X = -clamprad; X < clamprad; X++)
        {
            int x2 = X * X;
            for (int Z = -clamprad; Z < clamprad; Z++)
            {
                if (x2 + Z * Z < rad)
                {
                    for (int Y = 70; Y > 0; Y--)
                    {
                        FluidState fluid = worldObj.getFluidState(new BlockPos(X + posX, Y, Z + posZ));
                        BlockState block = worldObj.getBlockState(new BlockPos(X + posX, Y, Z + posZ));
                        if (block == Blocks.WATER.getDefaultState() || block == Blocks.LAVA.getDefaultState() || fluid != Fluids.EMPTY.getDefaultState() || block == Blocks.SEAGRASS.getDefaultState() || block == Blocks.TALL_SEAGRASS.getDefaultState() || block == Blocks.KELP.getDefaultState() || block == Blocks.KELP_PLANT.getDefaultState())
                        {
                            worldObj.setBlockState(new BlockPos(X + posX, Y, Z + posZ), Blocks.AIR.getDefaultState());
                        }
                    }
                }
            }
        }
    }

    public boolean update(EntityEgoBlast tsarblast)
    {
        //Rodol's awesome math that doesn't make the game lag when exploding
        if (n > 0 && n < nlimit)
        {
            boolean repeat = processChunk(lastposX, lastposZ);

            shell = (int) Math.floor((Math.sqrt(n) + 1) / 2);
            int shell2 = 2 * shell;
            leg = (int) Math.floor((n - (shell2 - 1) * (shell2 - 1)) / shell2);
            element = (n - (shell2 - 1) * (shell2 - 1)) - shell2 * leg - shell + 1;
            lastposX = leg == 0 ? shell : leg == 1 ? -element : leg == 2 ? -shell : element;
            lastposZ = leg == 0 ? element : leg == 1 ? shell : leg == 2 ? -element : -shell;
            n++;
            if (!repeat)
            {
                repeatCount++;
                if (repeatCount < Nukesfromthefuture.egoNukeSpeed.get() * 2) update(tsarblast);
                else
                {
                    repeatCount = 0;
                    return true;
                }
            }
        }
        else
        {
            tsarblast.hoool = null;
            tsarblast.remove();
        }
        return true;
    }

    private boolean processChunk(int x, int z)
    {
        processedchunks++;
        //System.out.println("processedchunks:" + processedchunks);
        double dist = x * x + z * z;
        if (dist < radius * radius)
        {
            dist = Math.sqrt(dist);
            int y = getTopBlock(x + posX, z + posZ, dist);
            float yele = posY + (y - posY) * 0.5f;
            if (Nukesfromthefuture.elevation.get()) yele = y;
            int ylimit = (int) Math.floor(yele - ((radius - dist) / 2) + (Math.sin(dist * 0.5) * 1.15));

            for (int Y = y; Y > ylimit; Y--)
            {
                if (Y == 0) break;
                BlockState block = worldObj.getBlockState(new BlockPos(x + posX, Y, z + posZ));
                FluidState fluid = worldObj.getFluidState(new BlockPos(x + posX, Y, z + posZ));
                worldObj.setBlockState(new BlockPos(x + posX, Y, z + posZ), Blocks.AIR.getDefaultState());
                //destroys any fluids that have been missed when the explosion gets initialized
                if(block == Blocks.WATER.getDefaultState() || block == Blocks.LAVA.getDefaultState() || fluid == Fluids.WATER.getDefaultState() || fluid == Fluids.FLOWING_WATER.getDefaultState() || fluid == Fluids.LAVA.getDefaultState() || fluid == Fluids.FLOWING_LAVA.getDefaultState()){
                    worldObj.setBlockState(new BlockPos(x + posX, Y, z + posZ), Blocks.AIR.getDefaultState());
                }
            }

            double limit = (radius / 2) + worldObj.rand.nextInt(radius / 4) + 7.5;
            if (dist < limit)
            {
                int blockType = worldObj.rand.nextInt(4) + 1;
                if (x >= 0 && z < 0) blockType = 1;
                if (x > 0 && z >= 0) blockType = 2;
                if (x <= 0 && z > 0) blockType = 3;
                if (x < 0 && z <= 0) blockType = 4;
                int metadata = (int) Math.ceil((16d / limit) * dist);
                metadata -= (radius / 10) - 1;
                if (metadata < 0) metadata = -metadata;
                metadata++;
                if (metadata > 15) metadata = 15;
                for (int Y = ylimit; Y > ylimit - (worldObj.rand.nextInt(5) + 2); Y--)
                {
                    if (Y == 0) break;
                    BlockState block = worldObj.getBlockState(new BlockPos(x + posX, Y, z + posZ));
                    if (blockType == 1) worldObj.setBlockState(new BlockPos(x + posX, Y, z + posZ), Nukesfromthefuture.trol.getDefaultState());
                    else if (blockType == 2) worldObj.setBlockState(new BlockPos(x + posX, Y, z + posZ), Nukesfromthefuture.egonium_ore.getDefaultState());
                    else if (blockType == 3) worldObj.setBlockState(new BlockPos(x + posX, Y, z + posZ), Nukesfromthefuture.trol.getDefaultState());
                    else worldObj.setBlockState(new BlockPos(x + posX, Y, z + posZ), Nukesfromthefuture.egonium_ore.getDefaultState());
                }
            }

            if (isTree)
            {
                isTree = false;
                int metadata = (int) Math.floor((16d / radius) * dist);
                if (metadata < 0) metadata = 0;
                metadata++;
                if (metadata > 15) metadata = 15;
                for (int Y = ylimit; Y > ylimit - treeHeight; Y--)
                {
                    if (Y == 0) break;
                    worldObj.setBlockState(new BlockPos(x + posX, Y, z + posZ), Nukesfromthefuture.trol.getDefaultState());
                }
            }

            return true;
        }
        else if (dist <= radius * radius * 1.3125 * 1.3125)
        {
            dist = Math.sqrt(dist);
            int y = getTopBlock(x + posX, z + posZ, dist);
            int ylimit = (int) Math.ceil(Math.sin((dist - radius - (radius / 16)) * radius * 0.001875) * (radius / 16));
            if (dist >= radius + 5)
            {
                int metadata = (int) Math.floor((16d / radius) * dist);
                if (metadata < 0) metadata = 0;
                metadata++;
                if (metadata > 15) metadata = 15;
                for (int Y = ylimit; Y >= 0; Y--)
                {
                    if (Y == 0) break;
                    int yy = Y + y;
                    BlockState blockID = worldObj.getBlockState(new BlockPos(x + posX, yy, z + posZ));
                    if (blockID == Nukesfromthefuture.lead_glass.getDefaultState());
                    else if (!isTree)
                    {
                        BlockState blockID1 = worldObj.getBlockState(new BlockPos(x + posX, yy - ylimit, z + posZ));
                        worldObj.setBlockState(new BlockPos(x + posX, yy, z + posZ), blockID1, 3);
                    }
                    else
                    {
                        isTree = false;
                        for (int Yy = 0; Yy >= -treeHeight; Yy--)
                        {
                            worldObj.setBlockState(new BlockPos(x + posX, yy + Yy, z + posZ), Nukesfromthefuture.trol.getDefaultState(), metadata, 3);
                        }
                        break;
                    }
                }
            }
            else
            {
                BlockState blockID = worldObj.getBlockState(new BlockPos(x + posX, y, z + posZ));
                if (blockID == Blocks.BEDROCK.getDefaultState())
                    ;
                else if (blockID != null && !blockID.isOpaqueCube(worldObj, new BlockPos(posX, posY, posZ))) worldObj.setBlockState(new BlockPos(x + posX, y, z + posZ), Blocks.AIR.getDefaultState());
                if (isTree)
                {
                    isTree = false;
                    int metadata = (int) Math.floor((16d / radius) * dist);
                    if (metadata < 0) metadata = 0;
                    metadata++;
                    if (metadata > 15) metadata = 15;
                    for (int Y = ylimit; Y > ylimit - treeHeight; Y--)
                    {
                        worldObj.setBlockState(new BlockPos(x + posX, Y, z + posZ), Nukesfromthefuture.egonium_ore.getDefaultState(), metadata, 3);
                    }
                }
            }
            return true;
        }
        return false;
    }

    private int getTopBlock(int x, int z, double dist)
    {
        int foundY = 0;
        boolean found = false;
        for (int y = 256; y > 0; y--)
        {
            BlockState blockID = worldObj.getBlockState(new BlockPos(x, y, z));
            //i don't know if this will work
            FluidState fluidID = worldObj.getFluidState(new BlockPos(x, y, z));
            if (blockID != Blocks.AIR.getDefaultState() || fluidID != Fluids.EMPTY.getDefaultState())
            {


                if (!blockID.isOpaqueCube(worldObj, new BlockPos(posX, posY, posZ)) || blockID == Blocks.OAK_LOG.getDefaultState() || blockID == Blocks.BIRCH_LOG.getDefaultState() || blockID == Blocks.DARK_OAK_LOG.getDefaultState() || blockID == Blocks.JUNGLE_LOG.getDefaultState() || blockID == Blocks.SPRUCE_LOG.getDefaultState() || blockID == Blocks.ACACIA_LOG.getDefaultState())
                {
                    worldObj.removeBlock(new BlockPos(x, y, z), false);
                    if (dist > radius / 2 && blockID == Blocks.OAK_LOG.getDefaultState()  || blockID == Blocks.BIRCH_LOG.getDefaultState() || blockID == Blocks.DARK_OAK_LOG.getDefaultState() || blockID == Blocks.JUNGLE_LOG.getDefaultState() || blockID == Blocks.SPRUCE_LOG.getDefaultState() || blockID == Blocks.ACACIA_LOG.getDefaultState()){
                        if(worldObj.getBlockState(new BlockPos(x, y - 1, z)) == Blocks.OAK_LOG.getDefaultState() || worldObj.getBlockState(new BlockPos(x, y - 1, z)) == Blocks.JUNGLE_LOG.getDefaultState() || worldObj.getBlockState(new BlockPos(x, y - 1, z)) == Blocks.DARK_OAK_LOG.getDefaultState() || worldObj.getBlockState(new BlockPos(x, y - 1, z)) == Blocks.ACACIA_LOG.getDefaultState() || worldObj.getBlockState(new BlockPos(x, y - 1, z)) == Blocks.BIRCH_LOG.getDefaultState() || worldObj.getBlockState(new BlockPos(x, y - 1, z)) == Blocks.SPRUCE_LOG.getDefaultState()) {
                            isTree = true;
                        }
                    }
                    if (!found && isTree)
                    {
                        foundY = y;
                        found = true;
                    }
                    continue;
                }
                else
                {
                    if (!found) return y;
                    else
                    {
                        treeHeight = foundY - y;
                        return foundY;
                    }
                }
            }
        }
        return foundY;
    }
}
