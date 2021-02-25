package net.nukesfromthefuture.tags;

import net.minecraft.block.Block;
import net.minecraft.data.TagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ForgeItemTagsProvider;

public class NffTags {
    public static Tags.IOptionalNamedTag<Item> tanks;
    public static void register() {

        tanks = registerTag("tanks");
    }
    public static Tags.IOptionalNamedTag<Item> registerTag(String name){
        return ItemTags.createOptional(new ResourceLocation("nff", name));
    }
}
