package net.nukesfromthefuture.main;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventHandler {
    @SubscribeEvent
    public static void itemRegisterThingy(RegistryEvent.Register<Item> gameRegistry){
        System.out.println("test thing or something idk");
        gameRegistry.getRegistry().register(Nukesfromthefuture.UwU);
        gameRegistry.getRegistry().register(Nukesfromthefuture.iTrol);
    }
    @SubscribeEvent
    public static void blockRegisterThingy(RegistryEvent.Register<Block> gameRegistry){
        System.out.println("test thing but block or something idk");
        gameRegistry.getRegistry().register(Nukesfromthefuture.trol);
    }
}
