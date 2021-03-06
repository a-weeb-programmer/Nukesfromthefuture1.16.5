package net.nukesfromthefuture.main;

import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.particle.BreakingParticle;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.data.advancements.AdventureAdvancements;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombieVillagerEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.player.SpawnLocationHelper;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.ITag;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagRegistryManager;
import net.minecraft.tileentity.BeaconTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ColorHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.fixes.SpawnEggNames;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.raid.Raid;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.ItemModelMesherForge;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.IRegistryDelegate;
import net.nukesfromthefuture.containers.*;
import net.nukesfromthefuture.entity.*;
import net.nukesfromthefuture.guiscreens.BetaScreen;
import net.nukesfromthefuture.guiscreens.ColliderScreen;
import net.nukesfromthefuture.guiscreens.EgoGuiScreen;
import net.nukesfromthefuture.guiscreens.GuiInfoContainer;
import net.nukesfromthefuture.handler.ContaminationWorldHandler;
import net.nukesfromthefuture.interfaces.Bugged;
import net.nukesfromthefuture.items.FluidTankItem;
import net.nukesfromthefuture.items.ItemFluidIdentidier;
import net.nukesfromthefuture.packet.AuxSavedData;
import net.nukesfromthefuture.packet.PacketDispatcher;
import net.nukesfromthefuture.packet.RadSurveyPacket;
import net.nukesfromthefuture.render.RenderPizzaCreep;
import net.nukesfromthefuture.saveddata.RadSavedData;
import net.nukesfromthefuture.tileentity.ColliderTile;
import net.nukesfromthefuture.tileentity.TileBeta;
import net.nukesfromthefuture.tileentity.TileEgoNuke;
import net.nukesfromthefuture.tileentity.TileNReactor;
import net.nukesfromthefuture.util.RadUtil;
import org.apache.logging.log4j.Level;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventHandler {

    @SubscribeEvent
    public static void tileEntityRegisterThing(RegistryEvent.Register<TileEntityType<?>> gameRegistry){
        Nukesfromthefuture.logger.log(Level.INFO, "tile entity register started");
        gameRegistry.getRegistry().register(TileEntityType.Builder.create(TileEgoNuke::new, Nukesfromthefuture.ego_nuke).build(null).setRegistryName("ego_thing"));
        gameRegistry.getRegistry().register(TileEntityType.Builder.create(TileBeta::new, Nukesfromthefuture.beta_nuke).build(null).setRegistryName("beta_thing"));
        gameRegistry.getRegistry().register(TileEntityType.Builder.create(ColliderTile::new, Nukesfromthefuture.singularity_nuke).build(null).setRegistryName("collider_tile"));
        gameRegistry.getRegistry().register(TileEntityType.Builder.create(TileNReactor::new, Nukesfromthefuture.nether_reactor, Nukesfromthefuture.nether_reactor_2).build(null).setRegistryName("nether_react_thing"));
    }
    @Bugged(bug = "The console output keeps saying no data fixer is registered for the entities, whatever that's supposed to mean. It's probably nothing")
    @SubscribeEvent
    public static void entityThings(RegistryEvent.Register<EntityType<?>> event){
        event.getRegistry().register(EntityType.Builder.<POTATOEntity>create(POTATOEntity::new, EntityClassification.MISC).size(0.25F, 0.25F).setTrackingRange(4).setShouldReceiveVelocityUpdates(true).setUpdateInterval(10).func_233608_b_(10).build("nff:potatoent").setRegistryName("potatoent"));
        event.getRegistry().register(EntityType.Builder.<EntityEgoBlast>create(EntityEgoBlast::new, EntityClassification.MISC).immuneToFire().size(0.05F, 0.05F).setShouldReceiveVelocityUpdates(true).setUpdateInterval(30).trackingRange(10).build("nff:ego_explod").setRegistryName("ego_explod"));
        event.getRegistry().register(EntityType.Builder.<MK3Explosion>create(MK3Explosion::new, EntityClassification.MISC).immuneToFire().trackingRange(4).size(0.25F, 0.25F).setShouldReceiveVelocityUpdates(true).setUpdateInterval(30).build("nff:generic_explosion").setRegistryName("generic_explosion"));
        event.getRegistry().register(EntityType.Builder.<Blast>create(Blast::new, EntityClassification.MISC).size(0.25F, 0.25F).immuneToFire().trackingRange(4).setShouldReceiveVelocityUpdates(true).setUpdateInterval(10).build("nff:blast").setRegistryName("blast"));
        event.getRegistry().register(Nukesfromthefuture.creeper);
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
        gameRegistry.getRegistry().register(Nukesfromthefuture.iNetherReactor);
        gameRegistry.getRegistry().register(Nukesfromthefuture.iReactor_2);
        gameRegistry.getRegistry().register(Nukesfromthefuture.iReactor_3);
        gameRegistry.getRegistry().register(Nukesfromthefuture.iRed_obs);
        gameRegistry.getRegistry().register(Nukesfromthefuture.pizza_creep_spawn);
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
        gameRegistry.getRegistry().register(Nukesfromthefuture.nether_reactor);
        gameRegistry.getRegistry().register(Nukesfromthefuture.nether_reactor_2);
        gameRegistry.getRegistry().register(Nukesfromthefuture.nether_reactor_burned_out);
        gameRegistry.getRegistry().register(Nukesfromthefuture.red_obsidian);
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
        RenderingRegistry.registerEntityRenderingHandler(Nukesfromthefuture.creeper, new RenderPizzaCreep.RenderFactory());
        ItemModelsProperties.registerProperty(Nukesfromthefuture.iEgo_nuke, new ResourceLocation(Nukesfromthefuture.mod_id, "is3d"), (stuff, more_stuff, idk_anymore) -> Nukesfromthefuture.gui_render_type);
        ItemModelsProperties.registerProperty(Nukesfromthefuture.iEgo_nuke, new ResourceLocation(Nukesfromthefuture.mod_id, "model_release_type"), (stuff, more_stuff, idk_anymore) -> Nukesfromthefuture.ego_model_type);
        ItemModelsProperties.registerProperty(Nukesfromthefuture.iBeta_nuke, new ResourceLocation(Nukesfromthefuture.mod_id, "is3d"), (stuff, more_stuff, idk_anymore) -> Nukesfromthefuture.gui_render_type);
    }
    @SubscribeEvent
    public static void serverStuff(FMLCommonSetupEvent event){
        PacketDispatcher.registerPackets();
        Forge_bus.registerOres();
        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(Nukesfromthefuture.creeper, EntityPizzaCreeper.registerAttributes().create());
        });
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
        @SubscribeEvent
        public static void playerStuff(LivingDeathEvent event){
            event.getEntityLiving().getPersistentData().putFloat("hfr_radiation", 0);

            if(event.getEntity() instanceof Entity && event.getSource() == NffDamageSource.POTATO_throw){
                for(Object o : event.getEntity().world.getPlayers()){
                    ServerPlayerEntity player = (ServerPlayerEntity) o;
                    Nukesfromthefuture.rad_trigger.trigger(player);
                }
            }

        }
        @SubscribeEvent
        public static void tickStuff(TickEvent.WorldTickEvent event){


                if (event.world != null && !event.world.isRemote && Nukesfromthefuture.enableRad.get()) {

                    int thunder = AuxSavedData.getThunder(event.world);

                    if (thunder > 0)
                        AuxSavedData.setThunder(event.world, thunder - 1);

                    RadSavedData dataa = RadSavedData.getData((ServerWorld) event.world);
                    double origin_x = dataa.origin_x;
                    double origin_z = dataa.origin_z;
                    double origin_y = event.world.getHeight(Heightmap.Type.WORLD_SURFACE, (int) origin_x, (int) origin_z);
                    double radius = dataa.radius;
                    if (!event.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(origin_x - radius, origin_y - 20, origin_z - radius, origin_x + radius, origin_y + 20, origin_z + radius)).isEmpty()) {

                        RadSavedData data = RadSavedData.getData((ServerWorld) event.world);

                        if (data.worldObj == null) {
                            data.worldObj = event.world;
                        }

                        for (Object o : event.world.getPlayers()) {

                            if (o instanceof ServerPlayerEntity) {
                                ServerPlayerEntity player = (ServerPlayerEntity) o;
                                PacketDispatcher.sendTo(new RadSurveyPacket(player.getPersistentData().getFloat("hfr_radiation")), player);
                            }
                        }

                        if (event.world.getDayTime() % 20 == 0 && event.phase == TickEvent.Phase.START) {
                            data.updateSystem();
                        }

                        List<Object> oList = new ArrayList<Object>();
                        oList.addAll(event.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(origin_x - radius, origin_y - 20, origin_z - radius, origin_x + radius, origin_y + 20, origin_z + radius)));

                        for (Object e : oList) {
                            if (e instanceof LivingEntity) {

                                //effect for radiation
                                LivingEntity entity = (LivingEntity) e;

                                if (event.world.getDayTime() % 20 == 0) {

                                    Chunk chunk = entity.world.getChunk((int) entity.getPosX(), (int) entity.getPosZ());
                                    float rad = data.getRadNumFromCoord(chunk.getPos().x, chunk.getPos().z);


                                    if (rad > 0) {
                                        RadUtil.applyRadData(entity, rad / 2);
                                    }

                                    if (entity.world.isRaining() && Nukesfromthefuture.cont.get() > 0 && AuxSavedData.getThunder(entity.world) > 0 &&
                                            entity.world.canBlockSeeSky(new BlockPos(MathHelper.floor(entity.getPosX()), MathHelper.floor(entity.getPosY()), MathHelper.floor(entity.getPosZ())))) {

                                        RadUtil.applyRadData(entity, Nukesfromthefuture.cont.get() * 0.005F);
                                    }
                                }

                                float eRad = entity.getPersistentData().getFloat("hfr_radiation");

                            /*if(entity instanceof EntityPizzaCreeper && eRad >= 200 && entity.getHealth() > 0) {

                                if(event.world.rand.nextInt(3) == 0 ) {
                                    EntityRadioCreeper creep = new EntityRadioCreeper(event.world);
                                    creep.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);

                                    if(!entity.isDead)
                                        if(!event.world.isRemote)
                                            event.world.spawnEntityInWorld(creep);
                                    entity.setDead();
                                } else {
                                    entity.attackEntityFrom(NffDamageSource.radiation_sickness, 100F);
                                }
                                continue;

                            } else*/
                                if (entity instanceof CowEntity && !(entity instanceof MooshroomEntity) && eRad >= 50) {
                                    MooshroomEntity creep = new MooshroomEntity(EntityType.MOOSHROOM, event.world);
                                    creep.setLocationAndAngles(entity.getPosX(), entity.getPosY(), entity.getPosZ(), entity.rotationYaw, entity.rotationPitch);

                                    if (entity.isAlive())
                                        if (!event.world.isRemote)
                                            event.world.addEntity(creep);
                                    entity.remove();
                                    continue;

                                } else if (entity instanceof VillagerEntity && eRad >= 500) {
                                    ZombieEntity creep = new ZombieEntity(event.world);
                                    creep.setLocationAndAngles(entity.getPosX(), entity.getPosY(), entity.getPosZ(), entity.rotationYaw, entity.rotationPitch);

                                    if (entity.isAlive())
                                        if (!event.world.isRemote)
                                            event.world.addEntity(creep);
                                    entity.remove();
                                    continue;
                                }

                                if (eRad < 200 || entity instanceof MooshroomEntity || entity instanceof ZombieEntity || entity instanceof SkeletonEntity)
                                    continue;

                                if (eRad > 2500)
                                    entity.getPersistentData().putFloat("hfr_radiation", 2500);

                                if (entity instanceof PlayerEntity && ((PlayerEntity) entity).abilities.isCreativeMode)
                                    continue;

                                if (eRad >= 1000) {
                                    if (entity.attackEntityFrom(NffDamageSource.nuke_blast, entity.getMaxHealth() * 100)) {
                                        entity.getPersistentData().putFloat("hfr_radiation", 0);

                                        if (entity instanceof ServerPlayerEntity) {
                                            Nukesfromthefuture.rad_death.trigger(((ServerPlayerEntity) entity));
                                        }
                                    }

                                    //.attackEntityFrom ensures the recentlyHit var is set to enable drops.
                                    //if the attack is canceled, then nothing will drop.
                                    //that's what you get for trying to cheat death
                                    entity.setHealth(0);

                                } else if (eRad >= 800) {
                                    if (event.world.rand.nextInt(300) == 0)
                                        entity.addPotionEffect(new EffectInstance(Effects.NAUSEA, 5 * 30, 0));
                                    if (event.world.rand.nextInt(300) == 0)
                                        entity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 10 * 20, 2));
                                    if (event.world.rand.nextInt(300) == 0)
                                        entity.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 10 * 20, 2));
                                    if (event.world.rand.nextInt(500) == 0)
                                        entity.addPotionEffect(new EffectInstance(Effects.POISON, 3 * 20, 2));
                                    if (event.world.rand.nextInt(700) == 0)
                                        entity.addPotionEffect(new EffectInstance(Effects.WITHER, 3 * 20, 1));
                                    if (event.world.rand.nextInt(300) == 0)
                                        entity.addPotionEffect(new EffectInstance(Effects.HUNGER, 5 * 20, 3));

                                } else if (eRad >= 600) {
                                    if (event.world.rand.nextInt(300) == 0)
                                        entity.addPotionEffect(new EffectInstance(Effects.NAUSEA, 5 * 30, 0));
                                    if (event.world.rand.nextInt(300) == 0)
                                        entity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 10 * 20, 2));
                                    if (event.world.rand.nextInt(300) == 0)
                                        entity.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 10 * 20, 2));
                                    if (event.world.rand.nextInt(500) == 0)
                                        entity.addPotionEffect(new EffectInstance(Effects.POISON, 3 * 20, 1));
                                    if (event.world.rand.nextInt(300) == 0)
                                        entity.addPotionEffect(new EffectInstance(Effects.HUNGER, 3 * 20, 3));

                                } else if (eRad >= 400) {
                                    if (event.world.rand.nextInt(300) == 0)
                                        entity.addPotionEffect(new EffectInstance(Effects.NAUSEA, 5 * 30, 0));
                                    if (event.world.rand.nextInt(500) == 0)
                                        entity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 5 * 20, 0));
                                    if (event.world.rand.nextInt(300) == 0)
                                        entity.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 5 * 20, 1));
                                    if (event.world.rand.nextInt(500) == 0)
                                        entity.addPotionEffect(new EffectInstance(Effects.HUNGER, 3 * 20, 2));

                                } else if (eRad >= 200) {
                                    if (event.world.rand.nextInt(300) == 0)
                                        entity.addPotionEffect(new EffectInstance(Effects.NAUSEA, 5 * 20, 0));
                                    if (event.world.rand.nextInt(500) == 0)
                                        entity.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 5 * 20, 0));
                                    if (event.world.rand.nextInt(700) == 0)
                                        entity.addPotionEffect(new EffectInstance(Effects.HUNGER, 3 * 20, 2));

                                    if (entity instanceof ServerPlayerEntity)
                                        Nukesfromthefuture.sickness.trigger(((ServerPlayerEntity) entity));
                                }
                            }

                    }
                }
                if (event.phase == TickEvent.Phase.START) {
                    ContaminationWorldHandler.handleWorldDestruction(event.world);
                }
            }
        }
        @SubscribeEvent
        public static void playerLogInStuff(PlayerEvent.PlayerLoggedInEvent event){
            if(!event.getPlayer().world.isRemote){
                event.getPlayer().sendStatusMessage(new StringTextComponent(TextFormatting.DARK_RED + "Thanks for installing Nukes from the Future UwU"), false);
                for(Object o : event.getPlayer().world.getPlayers()){
                    PlayerEntity player = (PlayerEntity) o;
                    if(player instanceof ServerPlayerEntity){
                        Nukesfromthefuture.installed.trigger(((ServerPlayerEntity) player));
                    }
                }
            }
        }
    }
}
