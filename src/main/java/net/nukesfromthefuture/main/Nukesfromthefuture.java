package net.nukesfromthefuture.main;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.nukesfromthefuture.blocks.TrollBlock;
import net.nukesfromthefuture.tabs.UselessTab;

@Mod("nff")
public class Nukesfromthefuture {
    public static final String mod_id = "nff";
    public static Item UwU;
    public static Block trol;
    //i hate item blocks, item blocks can die
    public static Item iTrol;
    public Nukesfromthefuture(){
        UwU = new Item(new Item.Properties().group(stuff)).setRegistryName("owo");
        trol = new TrollBlock(AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(0.3F, 0F)).setRegistryName("trol");
        iTrol = new BlockItem(trol, new Item.Properties().group(stuff)).setRegistryName("trol");
    }
    ItemGroup stuff = new UselessTab("uselessStuff");
}
