package net.nukesfromthefuture.tags;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class NffTags {
    public static Tags.IOptionalNamedTag<Block> tanks;
    public static void register() {

        tanks = registerTag("tanks");
    }
    public static Tags.IOptionalNamedTag<Block> registerTag(String name){
        return BlockTags.createOptional(new ResourceLocation("nff", name));
    }
}
