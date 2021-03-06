package net.nukesfromthefuture.saveddata;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.datafix.fixes.SavedDataUUID;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Dimension;
import net.minecraft.world.ForcedChunksSaveData;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.lighting.SkyLightStorage;
import net.minecraft.world.raid.Raid;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.FolderName;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraft.world.storage.WorldSavedDataCallableSave;
import net.minecraftforge.common.util.ChunkCoordComparator;
import net.minecraftforge.common.util.WorldCapabilityData;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.fml.loading.FMLPaths;
import net.nukesfromthefuture.entity.EntityPizzaCreeper;
import net.nukesfromthefuture.main.Nukesfromthefuture;
import net.nukesfromthefuture.packet.AuxParticlePacket;
import net.nukesfromthefuture.packet.PacketDispatcher;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RadSavedData extends WorldSavedData {
    public HashMap<ChunkPos, Float> contamination = new HashMap();
    public static double radius;
    public static double origin_x;
    public static double origin_z;
    //in order to reduce read operations
    private static RadSavedData openInstance;

    public World worldObj;

    public RadSavedData(String p_i2141_1_) {
        super(p_i2141_1_);
    }

    public RadSavedData(World p_i1678_1_)
    {
        super("rads");
        this.worldObj = p_i1678_1_;
        this.markDirty();
    }

    public RadSavedData() {
        super("rads");
        this.markDirty();
    }

    public void jettisonData() {

        contamination.clear();
        this.markDirty();
    }

    public void setRadForCoord(int x, int y, float radiation) {
        PlayerEntity player = Minecraft.getInstance().player;
        ChunkPos pair = new ChunkPos(x, y);
        contamination.put(pair, radiation);
        this.markDirty();

    }

    public float getRadNumFromCoord(int x, int y) {

        ChunkPos pair = new ChunkPos(x, y);
        Float rad = contamination.get(pair);

        return rad == null ? 0 : rad;
    }

    public void updateSystem() {

        HashMap<ChunkPos, Float> tempList = new HashMap(contamination);
        contamination.clear();

        for(Map.Entry<ChunkPos, Float> struct : tempList.entrySet()) {

            if(struct.getValue() != 0) {

                float rad = struct.getValue();

                //struct.radiation *= 0.999F;
                rad *= 0.999F;
                rad -= 0.05F;

                if(rad <= 0) {
                    rad = 0;
                }

                if(rad > Nukesfromthefuture.fogRad.get() && worldObj != null && worldObj.rand.nextInt(20) == 0 && !worldObj.getChunk(struct.getKey().x, struct.getKey().z).isEmpty()) {

                    int x = struct.getKey().x * 16 + worldObj.rand.nextInt(16);
                    int z = struct.getKey().z * 16 + worldObj.rand.nextInt(16);
                    int y = worldObj.getHeight(Heightmap.Type.WORLD_SURFACE, x, z) + worldObj.rand.nextInt(5);

                    PacketDispatcher.sendToAll(new AuxParticlePacket(x, y, z, 3));
                }

                if(rad > 1) {

                    float[] rads = new float[9];

                    rads[0] = getRadNumFromCoord(struct.getKey().x + 1, struct.getKey().z + 1);
                    rads[1] = getRadNumFromCoord(struct.getKey().x, struct.getKey().z + 1);
                    rads[2] = getRadNumFromCoord(struct.getKey().x - 1, struct.getKey().z + 1);
                    rads[3] = getRadNumFromCoord(struct.getKey().x - 1, struct.getKey().z);
                    rads[4] = getRadNumFromCoord(struct.getKey().x - 1, struct.getKey().z - 1);
                    rads[5] = getRadNumFromCoord(struct.getKey().x, struct.getKey().z - 1);
                    rads[6] = getRadNumFromCoord(struct.getKey().x + 1, struct.getKey().z - 1);
                    rads[7] = getRadNumFromCoord(struct.getKey().x + 1, struct.getKey().z);
                    rads[8] = getRadNumFromCoord(struct.getKey().x, struct.getKey().z);

                    float main = 0.6F;
                    float side = 0.075F;
                    float corner = 0.025F;

                    setRadForCoord(struct.getKey().x + 1, struct.getKey().z + 1, rads[0] + rad * corner);
                    setRadForCoord(struct.getKey().x, struct.getKey().z + 1, rads[1] + rad * side);
                    setRadForCoord(struct.getKey().x - 1, struct.getKey().z + 1, rads[2] + rad * corner);
                    setRadForCoord(struct.getKey().x - 1, struct.getKey().z, rads[3] + rad * side);
                    setRadForCoord(struct.getKey().x - 1, struct.getKey().z - 1, rads[4] + rad * corner);
                    setRadForCoord(struct.getKey().x, struct.getKey().z - 1, rads[5] + rad * side);
                    setRadForCoord(struct.getKey().x + 1, struct.getKey().z - 1, rads[6] + rad * corner);
                    setRadForCoord(struct.getKey().x + 1, struct.getKey().z, rads[7] + rad * side);
                    setRadForCoord(struct.getKey().x, struct.getKey().z, rads[8] + rad * main);

                } else {

                    this.setRadForCoord(struct.getKey().x, struct.getKey().z, getRadNumFromCoord(struct.getKey().x, struct.getKey().z) + rad);
                }
            }
        }

        this.markDirty();
    }

    @Override
    public void read(CompoundNBT nbt) {

        if(!Nukesfromthefuture.enableRad.get()) {
            return;
        }

        int count = nbt.getInt("radCount");

        for(int i = 0; i < count; i++) {

            ChunkPos pair = new ChunkPos(
                    nbt.getInt("cposX" + i),
                    nbt.getInt("cposZ" + i)

            );

            contamination.put(
                    pair,
                    nbt.getFloat("crad" + i)
            );
        }
    }
    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        nbt.putInt("radCount", contamination.size());

        int i = 0;

        for(Map.Entry<ChunkPos, Float> struct : contamination.entrySet()) {
            nbt.putInt("cposX" + i, struct.getKey().x);
            nbt.putInt("cposZ" + i, struct.getKey().z);
            nbt.putFloat("crad" + i, struct.getValue());

            i++;
        }
        return nbt;
    }

    public static RadSavedData getData(ServerWorld worldObj) {
        //What the actual hell am I doing
        DimensionSavedDataManager manager = worldObj.getSavedData();
        RadSavedData data = manager.getOrCreate(RadSavedData::new, "rads");
        data.worldObj = worldObj;
        openInstance  = data;

        return data;
    }

    public static void incrementRad(World worldObj, int x, int z, float rad, float maxRad, double radd, double originx, double originz) {
        RadSavedData data = getData((ServerWorld)worldObj);

        Chunk chunk = worldObj.getChunk(x, z);

        float r = data.getRadNumFromCoord(chunk.getPos().x, chunk.getPos().z);

        radius = radd;
        origin_x = originx;
        origin_z = originz;
        if(r < maxRad) {

            data.setRadForCoord(chunk.getPos().x, chunk.getPos().z, r + rad);
        }
    }

    public static void decrementRad(World worldObj, int x, int z, float rad, int radius, double originx, double originz) {

        RadSavedData data = getData((ServerWorld) worldObj);

        Chunk chunk = worldObj.getChunk(x, z);

        float r = data.getRadNumFromCoord(chunk.getPos().x, chunk.getPos().z);
        origin_x = originx;
        origin_z = originz;
        r -= rad;
        if(r > 0) {
            data.setRadForCoord(chunk.getPos().x, chunk.getPos().z, r);
        } else {
            data.setRadForCoord(chunk.getPos().x, chunk.getPos().z, 0);
        }
    }

}
