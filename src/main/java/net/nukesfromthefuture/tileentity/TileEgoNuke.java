package net.nukesfromthefuture.tileentity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.item.EggItem;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.nukesfromthefuture.main.Nukesfromthefuture;

public class TileEgoNuke extends TileEntity implements ITickableTileEntity{
    public TileEgoNuke() {
        super(Nukesfromthefuture.ego_type);
    }
    //thank god these methods didn't change
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return TileEntity.INFINITE_EXTENT_AABB;
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return 650D;
    }
    public void tick(){
        System.out.println("tick or something idk");
    }
}
