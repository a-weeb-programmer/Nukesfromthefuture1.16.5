package net.nukesfromthefuture.main;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod("nff")
public class Nukesfromthefuture {
    public static final String mod_id = "nff";
    public static Item UwU;
    public Nukesfromthefuture(){
        UwU = new Item(new Item.Properties().group(stuff)).setRegistryName("owo");
    }
    ItemGroup stuff = new ItemGroup("uselessStuff") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(UwU);
        }
    };
}
