package net.nukesfromthefuture.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class RadSurveyPacket {
    float rad;

    public RadSurveyPacket()
    {

    }

    public RadSurveyPacket(float rad)
    {
        this.rad = rad;
    }

    public RadSurveyPacket(ByteBuf buf) {

        rad = buf.readFloat();
    }

    public void toBytes(ByteBuf buf) {

        buf.writeFloat(rad);
    }


        public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

                PlayerEntity player = Minecraft.getInstance().player;
                player.getPersistentData().putFloat("hfr_radiation", rad);
            });
        ctx.get().setPacketHandled(true);
        }

}
