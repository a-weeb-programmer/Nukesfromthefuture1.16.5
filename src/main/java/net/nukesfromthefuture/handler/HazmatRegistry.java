package net.nukesfromthefuture.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

public class HazmatRegistry {
    public static final HazmatRegistry instance = new HazmatRegistry();
    private static HashMap<Item, Float> entries = new HashMap();

    private class HazmatEntry {

        private Item item;
        private float resistance;

        private HazmatEntry(Item item, float resistance) {
            this.item = item;
            this.resistance = resistance;
        }
    }

    public void registerHazmat(Item item, float resistance) {

        entries.put(item, resistance);
    }

    public float getResistance(ItemStack stack) {

        if(stack == null)
            return 0;

        float cladding = getCladding(stack);

        Float f = entries.get(stack.getItem());

        if(f != null)
            return f + cladding;

        return cladding;
    }

    public float getCladding(ItemStack stack) {

        if(stack.hasTag() && stack.getTag().getFloat("hfr_cladding") > 0)
            return stack.getTag().getFloat("hfr_cladding");

        return 0;
    }

    public float getResistance(PlayerEntity player) {

        float res = 0.0F;

        for(int i = 0; i < 4; i++) {
            res += getResistance(player.inventory.armorInventory.get(i));
        }



        return res;

    }
}
