package net.nukesfromthefuture.guiscreens;

import java.util.Arrays;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.nukesfromthefuture.main.Lib;

public abstract class GuiInfoContainer<T extends Container> extends ContainerScreen<T>{
	public T container;
	ResourceLocation guiUtil =  new ResourceLocation("nff:textures/gui/gui_utility.png");

	public GuiInfoContainer(T p_i1072_1_, PlayerInventory inv, ITextComponent text) {
		super(p_i1072_1_, inv, text);
	}
	
	public void drawFluidInfo(MatrixStack matrix, ITextComponent text, int x, int y) {
		this.func_243308_b(matrix, Arrays.asList(text), x, y);
	}
	
	public void drawElectricityInfo(MatrixStack stack,GuiInfoContainer gui, int mouseX, int mouseY, int x, int y, int width, int height, long power, long maxPower) {
		if(x <= mouseX && x + width > mouseX && y < mouseY && y + height >= mouseY)
			gui.drawFluidInfo(stack, new StringTextComponent(Lib.getShortNumber(power) + "/" + Lib.getShortNumber(maxPower) + "HE"), mouseX, mouseY);
	}
	
	public void drawCustomInfo(MatrixStack stack, GuiInfoContainer gui, int mouseX, int mouseY, int x, int y, int width, int height, ITextComponent text) {
		if(x <= mouseX && x + width > mouseX && y < mouseY && y + height >= mouseY)
			this.func_243308_b(stack, Arrays.asList(text), mouseX, mouseY);
	}
	
	public void drawCustomInfoStat(MatrixStack matrix, int mouseX, int mouseY, int x, int y, int width, int height, int tPosX, int tPosY, ITextComponent text) {
		
		if(x <= mouseX && x + width > mouseX && y < mouseY && y + height >= mouseY)
			this.func_243308_b(matrix,Arrays.asList(text), tPosX, tPosY);
	}
	
	public void drawInfoPanel(MatrixStack stack, int x, int y, int width, int height, int type) {

		Minecraft.getInstance().getTextureManager().bindTexture(guiUtil);
		
		switch(type) {
		case 0:
			//Small blue I
			blit(stack, x, y, 0, 0, 8, 8); break;
		case 1:
			//Small green I
			blit(stack, x, y, 0, 8, 8, 8); break;
		case 2:
			//Large blue I
			blit(stack, x, y, 8, 0, 16, 16); break;
		case 3:
			//Large green I
			blit(stack, x, y, 24, 0, 16, 16); break;
		case 4:
			//Small red !
			blit(stack, x, y, 0, 16, 8, 8); break;
		case 5:
			//Small yellow !
			blit(stack, x, y, 0, 24, 8, 8); break;
		case 6:
			//Large red !
			blit(stack, x, y, 8, 16, 16, 16); break;
		case 7:
			//Large yellow !
			blit(stack, x, y, 24, 16, 16, 16); break;
		case 8:
			//Small blue *
			blit(stack, x, y, 0, 32, 8, 8); break;
		case 9:
			//Small grey *
			blit(stack, x, y, 0, 40, 8, 8); break;
		case 10:
			//Large blue *
			blit(stack, x, y, 8, 32, 16, 16); break;
		case 11:
			//Large grey *
			blit(stack, x, y, 24, 32, 16, 16); break;
		}
	}
}
