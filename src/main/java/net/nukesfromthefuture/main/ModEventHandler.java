package net.nukesfromthefuture.main;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.item.Item;
import net.minecraft.tileentity.BeaconTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.nukesfromthefuture.tileentity.TileEgoNuke;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.util.ArrayList;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventHandler {
    //fine, i guess the new way you register things isn't that bad
    @SubscribeEvent
    public static void itemRegisterThingy(RegistryEvent.Register<Item> gameRegistry){
        Nukesfromthefuture.logger.log(Level.INFO, "Item register started");
        gameRegistry.getRegistry().register(Nukesfromthefuture.UwU);
        gameRegistry.getRegistry().register(Nukesfromthefuture.iTrol);
        gameRegistry.getRegistry().register(Nukesfromthefuture.pizza);
        gameRegistry.getRegistry().register(Nukesfromthefuture.cooked_POTATO);
        gameRegistry.getRegistry().register(Nukesfromthefuture.POTATO);
        gameRegistry.getRegistry().register(Nukesfromthefuture.iLead_glass);
        gameRegistry.getRegistry().register(Nukesfromthefuture.iEgo_nuke);
        gameRegistry.getRegistry().register(Nukesfromthefuture.lead_ingot);
        gameRegistry.getRegistry().register(Nukesfromthefuture.iEgonium_ore);
    }
    @SubscribeEvent
    public static void blockRegisterThingy(RegistryEvent.Register<Block> gameRegistry){
        Nukesfromthefuture.logger.log(Level.INFO, "block register started");
        gameRegistry.getRegistry().register(Nukesfromthefuture.trol);
        gameRegistry.getRegistry().register(Nukesfromthefuture.lead_glass);
        gameRegistry.getRegistry().register(Nukesfromthefuture.ego_nuke);
        gameRegistry.getRegistry().register(Nukesfromthefuture.egonium_ore);
    }
    @SubscribeEvent
    public static void tileEntityRegisterThing(RegistryEvent.Register<TileEntityType<?>> gameRegistry){
        Nukesfromthefuture.logger.log(Level.INFO, "tile entity register started");
        gameRegistry.getRegistry().register(TileEntityType.Builder.create(TileEgoNuke::new, Nukesfromthefuture.ego_nuke).build(null).setRegistryName("ego_thing"));
    }
    //You know what, I'll give 1.16 this: the way they seperate the client and common setup events are more genius than making proxys
    @SubscribeEvent
    public static void clientStuff(FMLClientSetupEvent event){
        System.out.println("client things or something, idk");
        RenderTypeLookup.setRenderLayer(Nukesfromthefuture.lead_glass, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(Nukesfromthefuture.ego_nuke, RenderType.getCutout());
    }
    @SubscribeEvent
    public static void serverStuff(FMLCommonSetupEvent event){
        Forge_bus.registerOres();
    }
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class Forge_bus {
        //FORGE, YOU'VE TAKEN THIS TOO FAR THIS TIME!
        private static final ArrayList<ConfiguredFeature<?, ?>> overworldOres = new ArrayList<ConfiguredFeature<?, ?>>();
        private static final ArrayList<ConfiguredFeature<?, ?>> netherOres = new ArrayList<ConfiguredFeature<?, ?>>();
        private static final ArrayList<ConfiguredFeature<?, ?>> endOres = new ArrayList<ConfiguredFeature<?, ?>>();

        public static void registerOres(){
            //BASE_STONE_OVERWORLD is for generating in stone, granite, diorite, and andesite
            //NETHERRACK is for generating in netherrack
            //BASE_STONE_NETHER is for generating in netherrack, basalt and blackstone

            //Overworld Ore Register
            overworldOres.add(register("ego_ore", Feature.ORE.withConfiguration(new OreFeatureConfig(
                    OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, Nukesfromthefuture.egonium_ore.getDefaultState(), 4)) //Vein Size
                    .range(30).square() //Spawn height start
                    .func_242731_b(20))); //Chunk spawn frequency
             //Chunk spawn frequency

            //Nether Ore Register

        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public static void gen(BiomeLoadingEvent event) {
            BiomeGenerationSettingsBuilder generation = event.getGeneration();
            if(event.getCategory().equals(Biome.Category.NETHER)){
                for(ConfiguredFeature<?, ?> ore : netherOres){
                    if (ore != null) generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ore);
                }
            }
            if(event.getCategory().equals(Biome.Category.THEEND)){
                for(ConfiguredFeature<?, ?> ore : endOres){
                    if (ore != null) generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ore);
                }
            }
            for(ConfiguredFeature<?, ?> ore : overworldOres){
                if (ore != null) generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ore);
            }
        }

        private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String name, ConfiguredFeature<FC, ?> configuredFeature) {
            return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, Nukesfromthefuture.mod_id + ":" + name, configuredFeature);
        }
    }
}
