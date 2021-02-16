package net.nukesfromthefuture.main;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.mojang.datafixers.types.Type;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.*;
import net.minecraft.tags.ITag;
import net.minecraft.tags.Tag;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.TypeReferences;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ObjectHolder;
import net.nukesfromthefuture.blocks.*;
import net.nukesfromthefuture.containers.*;
import net.nukesfromthefuture.entity.Blast;
import net.nukesfromthefuture.entity.EntityEgoBlast;
import net.nukesfromthefuture.entity.MK3Explosion;
import net.nukesfromthefuture.entity.POTATOEntity;
import net.nukesfromthefuture.items.*;
import net.nukesfromthefuture.packet.PacketDispatcher;
import net.nukesfromthefuture.tabs.FoodTab;
import net.nukesfromthefuture.tabs.ResourceTab;
import net.nukesfromthefuture.tabs.UselessTab;
import net.nukesfromthefuture.tabs.Weapons;
import net.nukesfromthefuture.tags.NffTags;
import net.nukesfromthefuture.tileentity.ColliderTile;
import net.nukesfromthefuture.tileentity.TileBeta;
import net.nukesfromthefuture.tileentity.TileEgoNuke;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod("nff")
public class Nukesfromthefuture {
    //I'm not capitializing variable or field names because I'm rejecting modernity and embracing tradition
    //I might organize this file tho... Nah
    public static final String mod_id = "nff";
    public static Item UwU;
    public static Item pizza;
    public static Block trol;
    public static Item fluid_barrel_empty;
    public static Item fluid_barrel_full;
    //ah, yes, the classic potato
    public static Item POTATO;
    public static Block waste;
    public static Block waste_wood;
    public static Block Deathinum_ore;
    public static Item unstable_pluto_identifier;
    public static Item cooked_POTATO;
    public static Block lead_glass;
    public static Block ego_nuke;
    public static Item lead_ingot;
    public static Block egonium_ore;
    //I HATE THE WAY THAT SUBITEMS NOW HAVE TO BE MADE INTO SEPERATE ITEM VARIABLES!!! I missed it when you could just override the getSubItems method
    public static Item empty_identifier;
    public static Item ego_fluid_identifier;
    public static Item ego_ingot;
    public static Block singularity_nuke;
    public static Item energy_extractor;
    public static Item nuke_rod;
    public static Item black_hole_tank;
    public static Item ego_tank;
    public static Item static_donut;
    public static Block beta_nuke;
    //i hate item blocks, item blocks can die
    public static Item iTrol;
    public static Item iLead_glass;
    public static Item iEgo_nuke;
    public static Item iWaste;
    public static Item iSingularity_nuke;
    public static Item iWasteWood;
    public static Item iDeathinum;
    public static Item iEgonium_ore;
    public static Item iBeta_nuke;
    //tile entity types.... WHY FORGE!!! WHAT WAS SO WRONG WITH REGISTERING TILE ENTITIES USING THE GAME REGISTRY!?
    @ObjectHolder("nff:ego_thing")
    public static TileEntityType<TileEgoNuke> ego_type;
    @ObjectHolder("nff:beta_thing")
    public static TileEntityType<TileBeta> beta_type;
    @ObjectHolder("nff:collider_tile")
    public static TileEntityType<ColliderTile> collider_tile;
    //entity types
    @ObjectHolder("nff:ego_explod")
    public static EntityType<EntityEgoBlast> ego_explod;
    @ObjectHolder("nff:generic_explosion")
    public static EntityType<MK3Explosion> generic;
    @ObjectHolder("nff:potatoent")
    public static EntityType<POTATOEntity> POTATOE;
    @ObjectHolder("nff:blast")
    public static EntityType<Blast> blast;
    //container types. ik, I'm really unorganized cramming all the contents into the main mod class lol
    @ObjectHolder("nff:ego_container")
    public static ContainerType<EgoContainer> ego_container;
    @ObjectHolder("nff:beta_container")
    public static ContainerType<BetaContainer> beta_container;
    @ObjectHolder("nff:colider_container")
    public static ContainerType<ColliderContainer> collider_container;
    //config values
    public static ForgeConfigSpec.BooleanValue elevation;
    public static ForgeConfigSpec.IntValue egoNukeSpeed;
    public static ForgeConfigSpec.IntValue egoStrength;
    public static ForgeConfigSpec.IntValue beta_strength;
    public static ForgeConfigSpec.IntValue beta_speed;
    public static ForgeConfigSpec.IntValue mk4;
    public static ForgeConfigSpec.IntValue singularity_strength;
    public static ForgeConfigSpec.IntValue singularity_speed;
    //logger
    public static Logger logger = LogManager.getLogger();
    //config
    public static ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec config;
    //misc
    public static World getWorld;
    static{
        egoStrength = builder.defineInRange("ego_nuke_strength", 300, 1, 1000);
        elevation = builder.define("elevation", true);
        egoNukeSpeed = builder.defineInRange("ego_nuke_speed", 9, 1, 40);
        beta_strength = builder.defineInRange("beta_strength", 230, 1, 1000);
        beta_speed = builder.defineInRange("beta_speed", 13, 0, 1000);
        mk4 = builder.defineInRange("mk4", 1024, 0, 10000);
        singularity_strength = builder.defineInRange("singularity_nuke_strngth", 150, 0, 1000);
        singularity_speed = builder.defineInRange("singularity_speed", 13, 0, 1000);
        config = builder.build();
    }
    public static void loadConfigStuff(ForgeConfigSpec configThing, String path){
        Nukesfromthefuture.logger.log(Level.INFO, "Loading config file");
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave().writingMode(WritingMode.REPLACE).build();
        file.load();
        config.setConfig(file);
    }

    public Nukesfromthefuture(){
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, config);
        loadConfigStuff(config, FMLPaths.CONFIGDIR.get().resolve("nukesfromthefuture.toml").toString());
        config.save();
        UwU = new Item(new Item.Properties().group(stuff)).setRegistryName("owo");
        trol = new TrollBlock(AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(0.3F, 0F)).setRegistryName("trol");
        iTrol = new BlockItem(trol, new Item.Properties().group(stuff)).setRegistryName("trol");
        pizza = new Pizza(new Item.Properties().food(new Food.Builder().hunger(20).saturation(10F).meat().build()).group(food)).setRegistryName("pizza");
        POTATO = new POTATO(new Item.Properties().group(weapons).isImmuneToFire()).setRegistryName("potatoe");
        cooked_POTATO = new CookedPotato(new Item.Properties().group(food).food(new Food.Builder().hunger(20).saturation(10F).build())).setRegistryName("cooked_potatoe");
        lead_glass = new LeadGlass(AbstractBlock.Properties.create(Material.GLASS).hardnessAndResistance(5.0F, 6.0F).notSolid().noDrops()).setRegistryName("lead_glass");
        iLead_glass = new BlockItem(lead_glass, new Item.Properties().group(stuff)).setRegistryName("lead_glass");
        ego_nuke = new EgoNuke(Block.Properties.create(Material.IRON).notSolid().hardnessAndResistance(3.0F, 1F).sound(SoundType.ANVIL)).setRegistryName("ego_nuke");
        iEgo_nuke = new BlockItem(ego_nuke, new Item.Properties().group(weapons)).setRegistryName("ego_nuke");
        lead_ingot = new Lead_Ingot(new Item.Properties().group(resources)).setRegistryName("lead_ingot");
        egonium_ore = new EgoniumOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(6.0F, 2.0F).sound(SoundType.STONE)).setRegistryName("ego_ore");
        iEgonium_ore = new BlockItem(egonium_ore, new Item.Properties().group(resources)).setRegistryName("ego_ore");
        ego_ingot = new EgoIngot(new Item.Properties().group(resources)).setRegistryName("ego_ingot");
        static_donut = new Donut(new Item.Properties().group(food).food(new Food.Builder().hunger(6).saturation(2F).build())).setRegistryName("static_donut");
        Deathinum_ore = new Deathinum_Ore(AbstractBlock.Properties.create(Material.ROCK).sound(SoundType.STONE).hardnessAndResistance(10.0F, 5.0F).setRequiresTool()).setRegistryName("deathinum_ore");
        iDeathinum = new BlockItem(Deathinum_ore, new Item.Properties().group(resources)).setRegistryName("deathinum_ore");
        waste = new Waste(Block.Properties.create(Material.MISCELLANEOUS).sound(SoundType.GROUND).hardnessAndResistance(0.2F, 10.F)).setRegistryName("waste");
        iWaste = new BlockItem(waste, new Item.Properties().group(resources)).setRegistryName("waste");
        waste_wood = new Waste_wood(Block.Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(0.4F, 10F)).setRegistryName("waste_wood");
        iWasteWood = new BlockItem(waste_wood, new Item.Properties().group(resources)).setRegistryName("waste_wood");
        beta_nuke = new Beta(Block.Properties.create(Material.IRON).notSolid().hardnessAndResistance(2.0F, 10.0F).sound(SoundType.METAL)).setRegistryName("beta");
        iBeta_nuke = new BlockItem(beta_nuke, new Item.Properties().group(weapons)).setRegistryName("beta");
        ego_fluid_identifier = new ItemFluidIdentidier(new Item.Properties().group(resources), FluidHandler.FluidType.egonium).setRegistryName("ego_fluid_identifier");
        empty_identifier = new ItemFluidIdentidier(new Item.Properties().group(resources), FluidHandler.FluidType.None).setRegistryName("empty_identifier");
        unstable_pluto_identifier = new ItemFluidIdentidier(new Item.Properties().group(resources), FluidHandler.FluidType.unstable_plutonium).setRegistryName("unstable_identifier");
        ego_tank = new FluidTankItem(FluidHandler.FluidType.egonium, new Item.Properties().group(resources)).setRegistryName("ego_tank");
        black_hole_tank = new FluidTankItem(FluidHandler.FluidType.BLACK_HOLE_FUEL, new Item.Properties().group(resources)).setRegistryName("black_hole_tank");
        singularity_nuke = new SingularityNuke(Block.Properties.create(Material.IRON).sound(SoundType.ANVIL).hardnessAndResistance(2.0F, 3.0F).notSolid()).setRegistryName("singularity_nuke");
        iSingularity_nuke = new BlockItem(singularity_nuke, new Item.Properties().group(weapons)).setRegistryName("singularity_nuke");
        fluid_barrel_empty = new Item(new Item.Properties().group(resources)).setRegistryName("empty_tank");
        FluidContainerRegistry.registerContainer(new FluidContainer(new ItemStack(Nukesfromthefuture.ego_tank), new ItemStack(Nukesfromthefuture.fluid_barrel_empty), FluidHandler.FluidType.egonium, 16000));
        FluidContainerRegistry.registerContainer(new FluidContainer(new ItemStack(Nukesfromthefuture.black_hole_tank), new ItemStack(Nukesfromthefuture.fluid_barrel_empty), FluidHandler.FluidType.BLACK_HOLE_FUEL, 16000));
        MinecraftForge.EVENT_BUS.addListener(ModEventHandler::clientStuff);
        MinecraftForge.EVENT_BUS.addListener(ModEventHandler::serverStuff);
        MinecraftForge.EVENT_BUS.register(new ModEventHandler());
        NffTags.register();

    }
    ItemGroup stuff = new UselessTab("uselessStuff");
    ItemGroup weapons = new Weapons("nffweapons");
    ItemGroup resources = new ResourceTab("nffresources");
    ItemGroup food = new FoodTab("foods");
}
