package net.nukesfromthefuture.packet;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.nukesfromthefuture.main.Nukesfromthefuture;

import java.util.Optional;
import java.util.function.Supplier;

public class PacketDispatcher {
    public static String version = "1";
    //WHAT THE HELL AM I DOING!!!??
    public static final SimpleChannel wrapper = NetworkRegistry.newSimpleChannel(new ResourceLocation(Nukesfromthefuture.mod_id, "mod_channel"), () -> version, version::equals, version::equals);
    /**Registers the packets. I don't even know if I'm doing this right*/
    public static final void registerPackets(){
        int i = 0;
        wrapper.registerMessage(i++, TEFluidPacket.class, TEFluidPacket::toBytes, TEFluidPacket::new, TEFluidPacket::handle, Optional.of( NetworkDirection.PLAY_TO_CLIENT));
    }
    // packet to player, called server side
    // specific player
    public static void sendTo(Object message, ServerPlayerEntity player) {

        wrapper.sendTo(message, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
    }

    // to players on that chunk, can also do by dinmension, etc check packetdistrubutor...
    public static void sendToChunk(Object message, Supplier<Chunk> chunk) { wrapper.send(PacketDistributor.TRACKING_CHUNK.with(chunk), message);}

    // to all players tracking the specified entity
    public static void sendToTrackingEntity(Object message, Supplier<Entity> entity) { wrapper.send(PacketDistributor.TRACKING_ENTITY.with(entity), message);}

    // all connected players
    public static void sendToAll(Object message) {
        wrapper.send(PacketDistributor.ALL.noArg(), message);
    }

    // packet to server, called client side
    public static void sendToServer(Object message) {
        PacketDispatcher.sendToServer(message);
    }


}
