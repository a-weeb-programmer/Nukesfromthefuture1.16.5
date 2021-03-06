package net.nukesfromthefuture.packet;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

import java.util.ArrayList;
import java.util.List;

public class AuxSavedData extends WorldSavedData {
    public List<DataPair> data = new ArrayList();

    public AuxSavedData(String p_i2141_1_) {
        super(p_i2141_1_);
    }

    public AuxSavedData()
    {
        super("nffauxdata");
        this.markDirty();
    }

    static class DataPair {

        String key = "";
        int value;

        public DataPair() { }

        public DataPair(String s, int i) {
            key = s;
            value = i;
        }

        void readFromNBT(CompoundNBT nbt, int i) {
            this.key = nbt.getString("aux_key_" + i);
            this.value = nbt.getInt("aux_val_" + i);
        }

        void writeToNBT(CompoundNBT nbt, int i) {
            nbt.putString("aux_key_" + i, key);
            nbt.putInt("aux_val_" + i, value);
        }

    }

    @Override
    public void read(CompoundNBT nbt) {

        int count = nbt.getInt("dCount");

        for(int i = 0; i < count; i++) {
            DataPair struct = new DataPair();
            struct.readFromNBT(nbt, i);

            data.add(struct);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {

        nbt.putInt("dCount", data.size());

        for(int i = 0; i < data.size(); i++) {
            data.get(i).writeToNBT(nbt, i);
        }
        return nbt;
    }

    public static AuxSavedData getData(ServerWorld worldObj) {
        DimensionSavedDataManager manager = worldObj.getSavedData();
        AuxSavedData data = manager.getOrCreate(AuxSavedData::new, "nffauxdata");

        return data;
    }

    public static void setThunder(World world, int dura) {
        AuxSavedData data = getData((ServerWorld) world);

        if(data.data == null) {
            data.data = new ArrayList();
            data.data.add(new DataPair("thunder", dura));

        } else {

            DataPair thunder = null;

            for(DataPair pair : data.data) {
                if(pair.key.equals("thunder")) {
                    thunder = pair;
                    break;
                }
            }

            if(thunder == null) {
                data.data.add(new DataPair("thunder", dura));
            } else {
                thunder.value = dura;
            }
        }

        data.markDirty();
    }

    public static int getThunder(World world) {

        AuxSavedData data = getData((ServerWorld) world);

        if(data == null)
            return 0;

        for(DataPair pair : data.data) {
            if(pair.key.equals("thunder")) {
                return pair.value;
            }
        }

        return 0;
    }
}
