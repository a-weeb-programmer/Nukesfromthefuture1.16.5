package net.nukesfromthefuture.main;

import com.mojang.datafixers.types.Type;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.TypeReferences;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import net.nukesfromthefuture.blocks.EgoNuke;
import net.nukesfromthefuture.blocks.EgoniumOre;
import net.nukesfromthefuture.blocks.LeadGlass;
import net.nukesfromthefuture.blocks.TrollBlock;
import net.nukesfromthefuture.items.CookedPotato;
import net.nukesfromthefuture.items.Lead_Ingot;
import net.nukesfromthefuture.items.Pizza;
import net.nukesfromthefuture.tabs.FoodTab;
import net.nukesfromthefuture.tabs.ResourceTab;
import net.nukesfromthefuture.tabs.UselessTab;
import net.nukesfromthefuture.items.POTATO;
import net.nukesfromthefuture.tabs.Weapons;
import net.nukesfromthefuture.tileentity.TileEgoNuke;

@Mod("nff")
public class Nukesfromthefuture {
    public static final String mod_id = "nff";
    public static Item UwU;
    public static Item pizza;
    public static Block trol;
    //ah, yes, the classic potato
    public static Item POTATO;
    public static Item cooked_POTATO;
    public static Block lead_glass;
    public static Block ego_nuke;
    public static Item lead_ingot;
    public static Block egonium_ore;
    //i hate item blocks, item blocks can die
    public static Item iTrol;
    public static Item iLead_glass;
    public static Item iEgo_nuke;
    public static Item iEgonium_ore;
    //tile entity types
    @ObjectHolder("nff:ego_thing")
    public static TileEntityType<TileEgoNuke> ego_type;
    public Nukesfromthefuture(){
        UwU = new Item(new Item.Properties().group(stuff)).setRegistryName("owo");
        trol = new TrollBlock(AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(0.3F, 0F)).setRegistryName("trol");
        iTrol = new BlockItem(trol, new Item.Properties().group(stuff)).setRegistryName("trol");
        pizza = new Pizza(new Item.Properties().food(new Food.Builder().hunger(20).saturation(10F).meat().build()).group(food)).setRegistryName("pizza");
        POTATO = new POTATO(new Item.Properties().group(weapons).isImmuneToFire()).setRegistryName("potatoe");
        cooked_POTATO = new CookedPotato(new Item.Properties().group(food).food(new Food.Builder().hunger(20).saturation(10F).build())).setRegistryName("cooked_potatoe");
        lead_glass = new LeadGlass(AbstractBlock.Properties.create(Material.GLASS).hardnessAndResistance(5.0F, 6.0F).notSolid().noDrops()).setRegistryName("lead_glass");
        iLead_glass = new BlockItem(lead_glass, new Item.Properties().group(stuff)).setRegistryName("lead_glass");
        ego_nuke = new EgoNuke(Block.Properties.create(Material.IRON).notSolid().hardnessAndResistance(3.0F, 1F)).setRegistryName("ego_nuke");
        iEgo_nuke = new BlockItem(ego_nuke, new Item.Properties().group(weapons)).setRegistryName("ego_nuke");
        lead_ingot = new Lead_Ingot(new Item.Properties().group(resources)).setRegistryName("lead_ingot");
        egonium_ore = new EgoniumOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(6.0F, 2.0F).sound(SoundType.STONE)).setRegistryName("ego_ore");
        iEgonium_ore = new BlockItem(egonium_ore, new Item.Properties().group(resources)).setRegistryName("ego_ore");
        MinecraftForge.EVENT_BUS.addListener(ModEventHandler::clientStuff);
        MinecraftForge.EVENT_BUS.addListener(ModEventHandler::serverStuff);
        MinecraftForge.EVENT_BUS.register(new ModEventHandler());

    }
    ItemGroup stuff = new UselessTab("uselessStuff");
    ItemGroup weapons = new Weapons("nffweapons");
    ItemGroup resources = new ResourceTab("nffresources");
    ItemGroup food = new FoodTab("foods");
}
