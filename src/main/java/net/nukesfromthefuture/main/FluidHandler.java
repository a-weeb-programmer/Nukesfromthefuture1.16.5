package net.nukesfromthefuture.main;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.INBTType;
import net.minecraft.util.text.ITextComponent;

import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class FluidHandler{
    public enum FluidType{
        None	(0x888888, 0, 1, 1, 0, 0, 0, EnumSymbol.NONE, "elitefluid.none"),
        coolant	(0x8FCADC, 7, 2, 1, 0, 0, 10, EnumSymbol.NONE, "elitefluid.coolant"),
        black_hole_fuel(0xA10000, 1, 2, 1, 0, 40, 40, EnumSymbol.NONE, "elitefluid.bhf"),
        lava(0xC93806, 3, 1, 1, 0, 0, 0, EnumSymbol.NONE, "elitefluid.lava"),
        paradoxium(0x38E8E5, 14, 2, 1, 0, 50, 100, EnumSymbol.NONE, "elitefluid.antitime"),
        troll_fluid(0x888888, 15, 1, 1, 50, 50, 50, EnumSymbol.NONE, "elitefluid.trol"),
        egonium(0xD82BFF, 12, 1, 1, 0, 60, 100, EnumSymbol.NONE, "elitefluid.egonium"),
        unstable_plutonium(0x1900FF, 5, 1, 1, 0, 100, 1000, EnumSymbol.RADIATION, "elitefluid.unstablePlutonium"),
        uranium(0x3EC400, 0, 2, 1, 30, 0, 10000, EnumSymbol.RADIATION, "elitefluid.uranium");

        //Approximate HEX Color of the fluid, used for pipe rendering
        private int color;
        //X position of the fluid on the sheet, the "row"
        private int textureX;
        //Y position of the fluid on the sheet, the "column"
        private int textureY;
        //ID of the texture sheet the fluid is on
        private int sheetID;
        //Unlocalized string ID of the fluid
        private String name;
        //Whether the fluid counts is too hot for certain tanks
        private boolean hot;
        public FluidType type;
        public CompoundNBT tag;
        //Whether the fluid counts as corrosive and requires a steel tank
        private boolean corrosive;
        //Whether the fluid is antimatter and requires magnetic storage
        private boolean antimatter;

        public int poison;
        public int flammability;
        public int reactivity;
        public EnumSymbol symbol;

        private FluidType(int color, int x, int y, int sheet, int p, int f, int r, EnumSymbol symbol, String name) {
            this.color = color;
            this.textureX = x;
            this.textureY = y;
            this.name = name;
            this.sheetID = sheet;
            this.poison = p;
            this.flammability = f;
            this.reactivity = r;
            this.symbol = symbol;
        }

        private FluidType(int color, int x, int y, int sheet, int p, int f, int r, EnumSymbol symbol, String name, boolean hot, boolean corrosive, boolean antimatter) {
            this.color = color;
            this.textureX = x;
            this.textureY = y;
            this.name = name;
            this.sheetID = sheet;
            this.poison = p;
            this.flammability = f;
            this.reactivity = r;
            this.symbol = symbol;
            this.hot = hot;
            this.corrosive = corrosive;
            this.antimatter = antimatter;
        }

        public int getColor() {
            return this.color;
        }
        public int getMSAColor() {
            return this.color;
        }
        public int textureX() {
            return this.textureX;
        }
        public int textureY() {
            return this.textureY;
        }
        public int getSheetID() {
            return this.sheetID;
        }
        public String getUnlocalizedName() {
            return this.name;
        }

        public static FluidType getEnum(int i) {
            if(i < FluidType.values().length)
                return FluidType.values()[i];
            else
                return FluidType.None;
        }

        public static FluidType getEnumFromName(String s) {

            for(int i = 0; i < FluidType.values().length; i++)
                if(FluidType.values()[i].getName().equals(s))
                    return FluidType.values()[i];

            return FluidType.None;
        }

        public int getID() {
            return Arrays.asList(FluidType.values()).indexOf(this);
        }

        public String getName() {
            return this.toString();
        }

        public boolean isHot() {
            return hot;
        }

        public boolean isCorrosive() {
            return corrosive;
        }

        public boolean isAntimatter() {
            return antimatter;
        }
        public CompoundNBT write(CompoundNBT nbt){
            nbt.putString("type", type.getName());
            return nbt;
        }
        public void read(CompoundNBT nbt){
            type = getEnumFromName(nbt.getString("type"));
        }
    };
}
