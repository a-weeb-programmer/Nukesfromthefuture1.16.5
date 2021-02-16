package net.nukesfromthefuture.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.ICustomPacket;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.simple.IndexedMessageCodec;
import net.nukesfromthefuture.interfaces.IFluidContainer;
import net.nukesfromthefuture.main.FluidHandler.FluidType;
import net.nukesfromthefuture.main.FluidHandler;
import org.lwjgl.system.windows.MSG;

import java.util.Arrays;
import java.util.function.Supplier;

public class TEFluidPacket {
    int x;
    int y;
    int z;
    int fill;
    int index;
    int type;



    public TEFluidPacket(int x, int y, int z, int fill, int index, FluidType type)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.fill = fill;
        this.index = index;
        this.type = Arrays.asList(FluidHandler.FluidType.values()).indexOf(type);
    }

    public TEFluidPacket(PacketBuffer buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        fill = buf.readInt();
        index = buf.readInt();
        type = buf.readInt();
    }


    public void toBytes(PacketBuffer buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeInt(fill);
        buf.writeInt(index);
        buf.writeInt(type);
    }


        public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            TileEntity te = Minecraft.getInstance().world.getTileEntity(new BlockPos(x, y, z));
            if (te != null && te instanceof IFluidContainer) {

                IFluidContainer gen = (IFluidContainer) te;
                gen.setFillstate(fill, index);
                gen.setType(FluidHandler.FluidType.getEnum(type), index);
            }
        });

        ctx.get().setPacketHandled(true);
        }
}
