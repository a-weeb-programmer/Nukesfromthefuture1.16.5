package net.nukesfromthefuture.guiscreens;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.nukesfromthefuture.containers.BetaContainer;
import net.nukesfromthefuture.tileentity.TileBeta;

public class BetaScreen extends ContainerScreen<BetaContainer> {
    public static ResourceLocation texture = new ResourceLocation("nff", "textures/gui/beta.png");
    TileBeta UwU;
    public BetaScreen(BetaContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.xSize = 175;
        this.ySize = 165;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        minecraft.getTextureManager().bindTexture(texture);
        this.blit(matrixStack, guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        this.font.drawString(matrixStack, this.title.getString(), this.xSize / 2 - font.getStringWidth(this.title.getString()) / 2, 5, 4210752);
    }
}
