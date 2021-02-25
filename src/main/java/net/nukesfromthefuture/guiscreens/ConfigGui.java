package net.nukesfromthefuture.guiscreens;

import net.minecraft.block.EndPortalBlock;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.tileentity.EndPortalTileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.ConfigGuiHandler;

public class ConfigGui extends Screen {


    protected ConfigGui(ITextComponent titleIn) {
        super(titleIn);
    }
}
