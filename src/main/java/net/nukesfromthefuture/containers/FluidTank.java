package net.nukesfromthefuture.containers;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.nukesfromthefuture.guiscreens.GuiInfoContainer;
import net.nukesfromthefuture.interfaces.Bugged;
import net.nukesfromthefuture.items.ItemFluidIdentidier;
import net.nukesfromthefuture.main.FluidHandler;
import net.nukesfromthefuture.main.FluidHandler.FluidType;
import net.nukesfromthefuture.packet.PacketDispatcher;
import net.nukesfromthefuture.packet.TEFluidPacket;
/**This is a special type of fluid tank that has been modified so
 * that there doesn't have to be an actual fluid, only a fluid type,
 * so don't get that confused with the fml FluidTank*/
public class FluidTank {
    FluidHandler.FluidType type;
    int fluid;
    int maxFluid;
    public int index;
    public static int x = 16;
    public static int y = 100;

    public FluidTank(FluidType type, int maxFluid, int index) {
        this.type = type;
        this.maxFluid = maxFluid;
        this.index = index;
    }

    public void setFill(int i) {
        fluid = i;
    }

    public void setTankType(FluidType type) {

        if(this.type.name().equals(type.name()))
            return;

        this.type = type;
        this.setFill(0);
    }

    public FluidType getTankType() {

        return type;
    }

    public int getFill() {
        return fluid;
    }

    public int getMaxFill() {
        return maxFluid;
    }

    public int changeTankSize(int size) {
        maxFluid = size;

        if(fluid > maxFluid) {
            int dif = fluid - maxFluid;
            fluid = maxFluid;
            return dif;
        }

        return 0;
    }

    //Called on TE update
    public void updateTank(int x, int y, int z, RegistryKey<World> dim) {
        PacketDispatcher.sendToAll(new TEFluidPacket(x, y, z, getFill(), index, type));
    }
    @Bugged(bug = "It won't load the fluid into the tank. I had to put code in the tick method of the tile entity that will fill up the fluid once the barrel is in the slot", possible_solutions = "there is probably something wrong with the fluid container registry")
    //Fills tank from canisters
    public void loadTank(ItemStack in, ItemStack out) {
        int size = in.getCount();
        int out_size = out.getCount();
        FluidType inType = FluidType.None;
        if(in != null) {
            inType = FluidContainerRegistry.getFluidType(in);



            if(FluidContainerRegistry.getFluidContent(in, type) <= 0)
                return;
        } else {
            return;
        }

        if(in != null && inType.name().equals(type.name()) && fluid + FluidContainerRegistry.getFluidContent(in, type) <= maxFluid) {
            if(out == null) {
                fluid += FluidContainerRegistry.getFluidContent(in, type);
                out = FluidContainerRegistry.getEmptyContainer(in);
                size--;
                if(size <= 0)
                    in = null;
            } else if(out != null && out.getItem() == FluidContainerRegistry.getEmptyContainer(in).getItem() && out_size < out.getMaxStackSize()) {
                fluid += FluidContainerRegistry.getFluidContent(in, type);
                size--;
                if(size <= 0)
                    in = null;
                out_size++;
            }
        }
    }

    //Fills canisters from tank
    public void unloadTank(int in, int out, ItemStack[] slots) {
        int size = slots[in].getCount();
        int out_size = slots[out].getCount();
        ItemStack full = null;
        if(slots[in] != null) {
            full = FluidContainerRegistry.getFullContainer(slots[in], type);
        }
        if(full == null)
            return;

        if(slots[in] != null && fluid - FluidContainerRegistry.getFluidContent(full, type) >= 0) {
            if(slots[out] == null) {
                fluid -= FluidContainerRegistry.getFluidContent(full, type);
                slots[out] = full.copy();
                size--;
                if(size <= 0)
                    slots[in] = null;
            } else if(slots[out] != null && slots[out].getItem() == FluidContainerRegistry.getFullContainer(slots[in], type).getItem() && out_size < slots[out].getMaxStackSize()) {
                fluid -= FluidContainerRegistry.getFluidContent(full, type);
                size--;
                if(size <= 0)
                    slots[in] = null;
                out_size++;
            }
        }





    }

    //Changes tank type
    public void setType(int in, int out, ItemStack[] slots) {

        if(slots[in] != null && slots[out] == null && slots[in].getItem() instanceof ItemFluidIdentidier) {
            for (ItemFluidIdentidier o : ItemFluidIdentidier.getIdentifier()) {
                FluidType newType = o.getType(slots[out]);

                if (!type.name().equals(newType.name())) {
                    type = newType;
                    slots[out] = slots[in].copy();
                    slots[in] = null;
                    fluid = 0;
                }
            }
        }
    }

    //Used in the GUI rendering, renders correct fluid type in container with progress
    public void renderTank(MatrixStack stack, ContainerScreen gui, int x, int y, int tx, int ty, int width, int height) {

        int i = (fluid * height) / maxFluid;
        gui.blit(stack, x, y - i, tx, ty - i, width, i);
    }

    public void renderTankInfo(MatrixStack stax, ContainerScreen gui, int mouseX, int mouseY, int x, int y, int width, int height) {
        if(gui instanceof GuiInfoContainer)
            renderTankInfo(stax, (GuiInfoContainer)gui, mouseX, mouseY, x, y, width, height);
    }

    public void renderTankInfo(MatrixStack matrix, GuiInfoContainer gui, int mouseX, int mouseY, int x, int y, int width, int height) {
        if(x <= mouseX && x + width > mouseX && y < mouseY && y + height >= mouseY)
            gui.drawFluidInfo(matrix, new StringTextComponent (I18n.format(this.type.getUnlocalizedName()) + fluid + "/" + maxFluid + "mB"), mouseX, mouseY);
    }

    public ResourceLocation getSheet() {
        return new ResourceLocation("nff:textures/gui/fluids" + this.type.getSheetID() + ".png");
    }

    //Called by TE to save fillstate
    public void writeToNBT(CompoundNBT nbt, String s) {
        nbt.putInt(s, fluid);
        nbt.putInt(s + "_max", maxFluid);
        nbt.putString(s + "_type", type.getName());
    }

    //Called by TE to load fillstate
    public void readFromNBT(CompoundNBT nbt, String s) {
        fluid = nbt.getInt(s);
        int max = nbt.getInt(s + "_max");
        if(max > 0)
            maxFluid = nbt.getInt(s + "_max");
        type = FluidType.getEnum(nbt.getInt(s + "_type"));
        if(type.name().equals(FluidType.None.name()))
            type = FluidType.getEnumFromName(nbt.getString(s + "_type"));
    }
}
