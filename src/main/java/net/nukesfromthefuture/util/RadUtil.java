package net.nukesfromthefuture.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;
import net.nukesfromthefuture.handler.HazmatRegistry;
import net.nukesfromthefuture.saveddata.RadSavedData;

public class RadUtil {
    /**
     * Calculates how much radiation can be applied to this entity by calculating resistance
     * @param entity
     * @return
     */
    public static float calculateRadiationMod(LivingEntity entity) {

        /*if(entity.isPotionActive(NftfPotion.mutation))
            return 0;*/

        if(entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)entity;

            float koeff = 5.0F;
            return (float) Math.pow(koeff, -HazmatRegistry.instance.getResistance(player));
        }

        return 1;
    }

    public static void applyRadData(Entity e, float f) {

        if(!(e instanceof LivingEntity))
            return;

        LivingEntity entity = (LivingEntity)e;

        f *= calculateRadiationMod(entity);

        float rad = e.getPersistentData().getFloat("hfr_radiation");
        e.getPersistentData().putFloat("hfr_radiation", Math.min(rad + f, 2500));
    }

    public static void applyRadDirect(Entity e, float f) {

        if(!(e instanceof LivingEntity))
            return;

       /* if(((LivingEntity)e).isPotionActive(NftfPotion.mutation))
            return;*/

        float rad = e.getPersistentData().getFloat("hfr_radiation");
        e.getPersistentData().putFloat("hfr_radiation", Math.min(rad + f, 2500));
    }

    public static float getRads(Entity e) {

        if(!(e instanceof LivingEntity))
            return 0.0F;

        return e.getPersistentData().getFloat("hfr_radiation");
    }

    public static void printGeigerData(PlayerEntity player) {

        World world = player.world;

        double eRad = ((int)(player.getPersistentData().getFloat("hfr_radiation") * 10)) / 10D;

        RadSavedData data = RadSavedData.getData((ServerWorld) player.world);
        Chunk chunk = world.getChunk((int)player.getPosX(), (int)player.getPosZ());
        double rads = ((int)(data.getRadNumFromCoord(chunk.getPos().x, chunk.getPos().z) * 10)) / 10D;

        double res = 100.0D - ((int)(RadUtil.calculateRadiationMod(player) * 10000)) / 100D;
        double resKoeff = ((int)(HazmatRegistry.instance.getResistance(player) * 100)) / 100D;

        String chunkPrefix = "";
        String radPrefix = "";
        String resPrefix = "" + TextFormatting.WHITE;

        if(rads == 0)
            chunkPrefix += TextFormatting.DARK_GREEN;
        else if(rads < 1)
            chunkPrefix += TextFormatting.YELLOW;
        else if(rads < 10)
            chunkPrefix += TextFormatting.GOLD;
        else if(rads < 100)
            chunkPrefix += TextFormatting.RED;
        else if(rads < 1000)
            chunkPrefix += TextFormatting.DARK_RED;
        else
            chunkPrefix += TextFormatting.DARK_GRAY;

        if(eRad < 200)
            radPrefix += TextFormatting.GREEN;
        else if(eRad < 400)
            radPrefix += TextFormatting.YELLOW;
        else if(eRad < 600)
            radPrefix += TextFormatting.GOLD;
        else if(eRad < 800)
            radPrefix += TextFormatting.RED;
        else if(eRad < 1000)
            radPrefix += TextFormatting.DARK_RED;
        else
            radPrefix += TextFormatting.DARK_GRAY;

        if(resKoeff > 0)
            resPrefix += TextFormatting.GREEN;

        player.sendStatusMessage(new StringTextComponent(TextFormatting.GOLD + "===== ☢ GEIGER COUNTER ☢ ====="), false);
        player.sendStatusMessage(new StringTextComponent(TextFormatting.YELLOW + "Current chunk radiation: " + chunkPrefix + rads + " RAD/s"), false);
        player.sendStatusMessage(new StringTextComponent(TextFormatting.YELLOW + "Player contamination: " + radPrefix + eRad + " RAD"), false);
        player.sendStatusMessage(new StringTextComponent(TextFormatting.YELLOW + "Player resistance: " + resPrefix + res + "% (" + resKoeff + ")"), false);
    }
}
