package net.nukesfromthefuture.guiscreens;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.nukesfromthefuture.containers.ColliderContainer;
import net.nukesfromthefuture.containers.FluidTank;
import net.nukesfromthefuture.main.Nukesfromthefuture;
import net.nukesfromthefuture.tileentity.ColliderTile;
import net.nukesfromthefuture.tileentity.TileBeta;
import org.lwjgl.opengl.GL11;

public class ColliderScreen extends GuiInfoContainer<ColliderContainer> {
    public ColliderTile ono;
    public ResourceLocation texture = new ResourceLocation(Nukesfromthefuture.mod_id, "textures/gui/singularity_nuke.png");
    public ColliderScreen(ColliderContainer p_i1072_1_, PlayerInventory inv, ITextComponent text) {
        super(p_i1072_1_, inv, text);
        xSize = 172;
        ySize = 166;
        ono = (ColliderTile) p_i1072_1_.player.world.getTileEntity(p_i1072_1_.te.getPos());
    }
    //imma treat this method like the new drawScreen from 1.7.10
    @Override
    public void render(MatrixStack matrixStack, int x, int y, float partialTicks) {
        renderBackground(matrixStack);
        super.render(matrixStack, x, y, partialTicks);
        renderHoveredTooltip(matrixStack, x, y);
        ono.tank.renderTankInfo(matrixStack, this, x, y, guiLeft + 147, guiTop + 53 - 48, 16, 48);

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bindTexture(texture);
        blit(matrixStack, guiLeft, guiTop, 0, 0, xSize, ySize);

        minecraft.getInstance().getTextureManager().bindTexture(ono.tank.getSheet());
        ono.tank.renderTank(matrixStack, this, guiLeft + 147, guiTop + 54, ono.tank.getTankType().textureX() * FluidTank.x, ono.tank.getTankType().textureY() * FluidTank.y, 16, 47);

    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        String name = this.title.getString();
        font.drawString(matrixStack, name, xSize / 2 - font.getStringWidth(name) / 2, 4, 4210752);
    }

}
