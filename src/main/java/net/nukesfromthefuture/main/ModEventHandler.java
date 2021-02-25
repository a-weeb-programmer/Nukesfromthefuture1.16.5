package net.nukesfromthefuture.main;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraft.advancements.AdvancementList;
import net.minecraft.advancements.criterion.TickTrigger;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.particle.BreakingParticle;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagRegistryManager;
import net.minecraft.tileentity.BeaconTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ColorHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.IRegistryDelegate;
import net.nukesfromthefuture.containers.*;
import net.nukesfromthefuture.entity.Blast;
import net.nukesfromthefuture.entity.EntityEgoBlast;
import net.nukesfromthefuture.entity.MK3Explosion;
import net.nukesfromthefuture.entity.POTATOEntity;
import net.nukesfromthefuture.guiscreens.BetaScreen;
import net.nukesfromthefuture.guiscreens.ColliderScreen;
import net.nukesfromthefuture.guiscreens.EgoGuiScreen;
import net.nukesfromthefuture.guiscreens.GuiInfoContainer;
import net.nukesfromthefuture.interfaces.Bugged;
import net.nukesfromthefuture.items.FluidTankItem;
import net.nukesfromthefuture.items.ItemFluidIdentidier;
import net.nukesfromthefuture.packet.PacketDispatcher;
import net.nukesfromthefuture.tileentity.ColliderTile;
import net.nukesfromthefuture.tileentity.TileBeta;
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
        gameRegistry.getRegistry().register(Nukesfromthefuture.ego_ingot);
        gameRegistry.getRegistry().register(Nukesfromthefuture.static_donut);
        gameRegistry.getRegistry().register(Nukesfromthefuture.iWaste);
        gameRegistry.getRegistry().register(Nukesfromthefuture.iWasteWood);
        gameRegistry.getRegistry().register(Nukesfromthefuture.iDeathinum);
        gameRegistry.getRegistry().register(Nukesfromthefuture.iBeta_nuke);
        gameRegistry.getRegistry().register(Nukesfromthefuture.empty_identifier);
        gameRegistry.getRegistry().register(Nukesfromthefuture.ego_fluid_identifier);
        gameRegistry.getRegistry().register(Nukesfromthefuture.unstable_pluto_identifier);
        gameRegistry.getRegistry().register(Nukesfromthefuture.ego_tank);
        gameRegistry.getRegistry().register(Nukesfromthefuture.black_hole_tank);
        gameRegistry.getRegistry().register(Nukesfromthefuture.iSingularity_nuke);
        gameRegistry.getRegistry().register(Nukesfromthefuture.fluid_barrel_empty);
        gameRegistry.getRegistry().register(Nukesfromthefuture.black_hole);
        gameRegistry.getRegistry().register(Nukesfromthefuture.singularity_magnet);
    }
    @SubscribeEvent
    public static void blockRegisterThingy(RegistryEvent.Register<Block> gameRegistry){
        Nukesfromthefuture.logger.log(Level.INFO, "block register started");
        gameRegistry.getRegistry().register(Nukesfromthefuture.trol);
        gameRegistry.getRegistry().register(Nukesfromthefuture.lead_glass);
        gameRegistry.getRegistry().register(Nukesfromthefuture.ego_nuke);
        gameRegistry.getRegistry().register(Nukesfromthefuture.egonium_ore);
        gameRegistry.getRegistry().register(Nukesfromthefuture.Deathinum_ore);
        gameRegistry.getRegistry().register(Nukesfromthefuture.waste);
        gameRegistry.getRegistry().register(Nukesfromthefuture.waste_wood);
        gameRegistry.getRegistry().register(Nukesfromthefuture.beta_nuke);
        gameRegistry.getRegistry().register(Nukesfromthefuture.singularity_nuke);
    }
    @SubscribeEvent
    public static void tileEntityRegisterThing(RegistryEvent.Register<TileEntityType<?>> gameRegistry){
        Nukesfromthefuture.logger.log(Level.INFO, "tile entity register started");
        gameRegistry.getRegistry().register(TileEntityType.Builder.create(TileEgoNuke::new, Nukesfromthefuture.ego_nuke).build(null).setRegistryName("ego_thing"));
        gameRegistry.getRegistry().register(TileEntityType.Builder.create(TileBeta::new, Nukesfromthefuture.beta_nuke).build(null).setRegistryName("beta_thing"));
        gameRegistry.getRegistry().register(TileEntityType.Builder.create(ColliderTile::new, Nukesfromthefuture.singularity_nuke).build(null).setRegistryName("collider_tile"));
    }
    @Bugged(bug = "The console output keeps saying no data fixer is registered for the entities, whatever that's supposed to mean. It's probably nothing")
    @SubscribeEvent
    public static void entityThings(RegistryEvent.Register<EntityType<?>> event){
        event.getRegistry().register(EntityType.Builder.<POTATOEntity>create(POTATOEntity::new, EntityClassification.MISC).size(0.25F, 0.25F).setTrackingRange(4).setShouldReceiveVelocityUpdates(true).setUpdateInterval(10).func_233608_b_(10).build("nff:potatoent").setRegistryName("potatoent"));
        event.getRegistry().register(EntityType.Builder.<EntityEgoBlast>create(EntityEgoBlast::new, EntityClassification.MISC).immuneToFire().size(0.05F, 0.05F).setShouldReceiveVelocityUpdates(true).setUpdateInterval(30).trackingRange(10).build("nff:ego_explod").setRegistryName("ego_explod"));
        event.getRegistry().register(EntityType.Builder.<MK3Explosion>create(MK3Explosion::new, EntityClassification.MISC).immuneToFire().trackingRange(4).size(0.25F, 0.25F).setShouldReceiveVelocityUpdates(true).setUpdateInterval(30).build("nff:generic_explosion").setRegistryName("generic_explosion"));
        event.getRegistry().register(EntityType.Builder.<Blast>create(Blast::new, EntityClassification.MISC).size(0.25F, 0.25F).immuneToFire().trackingRange(4).setShouldReceiveVelocityUpdates(true).setUpdateInterval(10).build("nff:blast").setRegistryName("blast"));
    }
    @SubscribeEvent
    public static void containerThings(RegistryEvent.Register<ContainerType<?>> event){
        event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            World world = inv.player.world;
            return new EgoContainer(windowId, pos, world, inv, inv.player);
        }).setRegistryName("ego_container"));
        event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            World world = inv.player.world;
            return new BetaContainer(windowId, pos, world, inv.player, inv);
        }).setRegistryName("beta_container"));
        event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            World world = inv.player.world;
            return new ColliderContainer(windowId, pos, world, inv.player, inv);
        }).setRegistryName("colider_container"));
    }
    @SubscribeEvent
    public static void clientStuff(FMLClientSetupEvent event){
        System.out.println("client things or something, idk");
        RenderTypeLookup.setRenderLayer(Nukesfromthefuture.lead_glass, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(Nukesfromthefuture.ego_nuke, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(Nukesfromthefuture.beta_nuke, RenderType.getCutout());
        ScreenManager.registerFactory(Nukesfromthefuture.ego_container, EgoGuiScreen::new);
        ScreenManager.registerFactory(Nukesfromthefuture.beta_container, BetaScreen::new);
        ScreenManager.registerFactory(Nukesfromthefuture.collider_container, ColliderScreen::new);
        RenderingRegistry.registerEntityRenderingHandler(Nukesfromthefuture.POTATOE, manager -> new SpriteRenderer<>(manager, Minecraft.getInstance().getItemRenderer()));
    }
    @SubscribeEvent
    public static void serverStuff(FMLCommonSetupEvent event){
        PacketDispatcher.registerPackets();
        Forge_bus.registerOres();
    }
    @SubscribeEvent
    public static void colorStuff(ColorHandlerEvent.Item event) {
        for (ItemFluidIdentidier fluid_identifier : ItemFluidIdentidier.getIdentifier()) {
            event.getItemColors().register((stack, color) -> {
                return fluid_identifier.getColor(color, stack);
            }, fluid_identifier);
        }
        for(FluidTankItem tank : FluidTankItem.getTanks()){
            event.getItemColors().register(tank::setColor, tank);
        }
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
