package net.nukesfromthefuture.packet;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class AuxParticlePacket {
    double x;
    double y;
    double z;
    int type;

    public AuxParticlePacket()
    {

    }

    public AuxParticlePacket(double x, double y, double z, int type)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
    }

    public AuxParticlePacket(ByteBuf buf) {
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
        type = buf.readInt();
    }

    public void toBytes(ByteBuf buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeInt(type);
    }


        public void handle(Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
            try {


            } catch(Exception x) { }

            });
            ctx.get().setPacketHandled(true);
        }

}
