package net.nukesfromthefuture.handler;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;
import net.nukesfromthefuture.saveddata.RadSavedData;

import java.util.Map;

public class ContaminationWorldHandler {
    public static void handleWorldDestruction(World world) {

        if(!(world instanceof ServerWorld) )
            return;

        ServerWorld serv = (ServerWorld) world;

        RadSavedData data = RadSavedData.getData(serv);
        ServerChunkProvider provider = (ServerChunkProvider) serv.getChunkProvider();

        int count = 50;//MainRegistry.worldRad;
        int threshold = 5;//MainRegistry.worldRadThreshold;

        Object[] entries = data.contamination.entrySet().toArray();

        if(entries.length == 0)
            return;

        Map.Entry<ChunkPos, Float> randEnt = (Map.Entry<ChunkPos, Float>) entries[world.rand.nextInt(entries.length)];

        ChunkPos coords = randEnt.getKey();

        for(int i = 0; i < 1; i++) {


            if(randEnt == null || randEnt.getValue() < threshold)
                continue;

            if(provider.chunkExists(coords.x, coords.z)) {

                for(int a = 0; a < 16; a ++) {
                    for(int b = 0; b < 16; b ++) {

                        if(world.rand.nextInt(3) != 0)
                            continue;

                        int x = coords.getRegionPositionX() - 8 + a;
                        int z = coords.getRegionPositionZ() - 8 + b;
                        int y = world.getHeight(Heightmap.Type.WORLD_SURFACE, x, z) - world.rand.nextInt(2);

                        if(world.getBlockState(new BlockPos(x, y, z)) == Blocks.GRASS_BLOCK.getDefaultState()) {
                            world.setBlockState(new BlockPos(x, y, z), Blocks.DIRT.getDefaultState());
                        } else if(world.getBlockState(new BlockPos(x, y, z)) == Blocks.TALL_GRASS.getDefaultState()) {
                            world.removeBlock(new BlockPos(x, y, z), false);
                        } else if(world.getBlockState(new BlockPos(x, y, z)) == Blocks.DARK_OAK_LEAVES.getDefaultState()) {
                            world.removeBlock(new BlockPos(x, y, z), false);
                        } else if(world.getBlockState(new BlockPos(x, y, z)) == Blocks.OAK_LEAVES.getDefaultState()) {
                            world.removeBlock(new BlockPos(x, y, z), false);
                        }
                    }
                }
            }
        }
    }
}
