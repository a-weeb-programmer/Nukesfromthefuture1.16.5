package net.nukesfromthefuture.entity;

import net.minecraft.command.arguments.NBTCompoundTagArgument;
import net.minecraft.command.arguments.NBTTagArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityPosWrapper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.nukesfromthefuture.explosion.EgoExplosion;
import net.nukesfromthefuture.main.Nukesfromthefuture;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class EntityEgoBlast extends Entity {
    public EgoExplosion	hoool = null;
    public int		radis;
    public int			time		= 0;

    public EntityEgoBlast(World par1World)
    {
        super(Nukesfromthefuture.ego_explod, par1World);
        ignoreFrustumCheck = true;
    }

    public EntityEgoBlast(World par1World, float x, float y, float z, EgoExplosion hool, int rad)
    {
        super(Nukesfromthefuture.ego_explod, par1World);
        ignoreFrustumCheck = true;
        hoool = hool;
        radis = rad;
        lastTickPosX = Math.sqrt(radis - Nukesfromthefuture.egoStrength.get()) / 10;
        setPosition(x, y, z);
    }

    public EntityEgoBlast(World par1World, double x, double y, double z, float rad)
    {
        super(Nukesfromthefuture.ego_explod, par1World);
        ignoreFrustumCheck = true;
        radis = (int) rad;
        lastTickPosX = Math.sqrt(rad - Nukesfromthefuture.egoStrength.get()) / 10;
        setPosition(x, y, z);
    }

    public EntityEgoBlast(EntityType<Entity> entityEntityType, World world) {
        super(Nukesfromthefuture.ego_explod, world);
    }


    @Override
    protected void registerData() {

    }

    @Override
    public void tick()
    {
        super.tick();

        if (world.rand.nextInt(10) == 0)
        {
            world.playSound(this.getPosX(), this.getPosY(), this.getPosZ(), new SoundEvent(new ResourceLocation("ambient.weather.thunder")), SoundCategory.WEATHER, 10.0F, 0.50F, false);
            if(rand.nextInt(5) == 0)
                this.world.playSound(this.getPosX(), this.getPosY(), this.getPosZ(), new SoundEvent(new ResourceLocation("random.explode")), SoundCategory.MASTER, 10000.0F, 0.8F + this.rand.nextFloat() * 0.2F, false);
        }

        ticksExisted++;

        if (!world.isRemote)
        {
            if (hoool == null && ticksExisted > 1200) setDead();
            if (ticksExisted % 20 == 0) updateEntityList();
            if (ticksExisted < 1200 && ticksExisted % 5 == 0) pushAndHurtEntities();
            for (int i = 0; i < Nukesfromthefuture.egoNukeSpeed.get() * 2; i++)
            {
                if (hoool != null)
                {
                    hoool.update(this);
					/*if (tsar.update())
					{
						tsar = null;
					}*/
                }
                else
                {
                    return;
                }
            }
        }
    }

    List<Entity> entitylist = new ArrayList<Entity>();

    public void updateEntityList()
    {
        entitylist.clear();
        double ldist = radis*radis;
        for (int i = 0; i < world.getLoadedEntitiesWithinAABB(Entity.class, TileEntity.INFINITE_EXTENT_AABB).size(); i++)
        {
            Entity e = (Entity) world.getLoadedEntitiesWithinAABB(Entity.class, TileEntity.INFINITE_EXTENT_AABB).get(i);
            double dist = e.getDistanceSq(getPosX(),getPosY(),getPosZ());
            if (dist < ldist)
            {
                if ((e instanceof PlayerEntity && ((PlayerEntity) e).abilities.isCreativeMode) || e instanceof EntityEgoBlast || e == this) continue;
                entitylist.add(e);
            }
        }
    }

    public void pushAndHurtEntities()
    {
        int radius = radis * Nukesfromthefuture.egoStrength.get() * 1;
        if (radius > 80) radius = 80;
        int var3 = MathHelper.floor(getPosX() - radius - 1.0D);
        int var4 = MathHelper.floor(getPosX() + radius + 1.0D);
        int var5 = MathHelper.floor(getPosY() - radius - 1.0D);
        int var28 = MathHelper.floor(getPosY() + radius + 1.0D);
        int var7 = MathHelper.floor(getPosZ() - radius - 1.0D);
        int var29 = MathHelper.floor(getPosZ() + radius + 1.0D);
        List var9 = world.getEntitiesWithinAABBExcludingEntity(this, AxisAlignedBB.toImmutable(new MutableBoundingBox(var3, var5, var7, var4, var28, var29)));
        Vector3d var30 = new Vector3d(getPosX(), getPosY(), getPosZ());

        for (int var11 = 0; var11 < var9.size(); ++var11)
        {
            Entity var31 = (Entity) var9.get(var11);
            double var13 = var31.getDistance(this) / radius;

            if (var13 <= 1.0D)
            {
                double var15 = var31.getPosX() - getPosX();
                double var17 = var31.getPosY() + var31.getEyeHeight() - getPosY();
                double var19 = var31.getPosZ() - getPosZ();
                double var33 = MathHelper.sqrt(var15 * var15 + var17 * var17 + var19 * var19);

                if (var33 != 0.0D)
                {
                    var15 /= var33;
                    var17 /= var33;
                    var19 /= var33;
                    if (!(var31 instanceof EntityEgoBlast) && !(var31 instanceof EntityEgoBlast))
                    {
                        if (var31 instanceof FallingBlockEntity) var31.remove();
                        else
                        {
                            if (var31 instanceof PlayerEntity && ((PlayerEntity) var31).abilities.isCreativeMode) continue;
                            var31.attackEntityFrom(DamageSource.FALL, 16 * radius);
                            var31.lastTickPosX -= var15 * 8;
                            var31.lastTickPosY -= var17 * 8;
                            var31.lastTickPosZ -= var19 * 8;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void read(CompoundNBT nbt)
    {
        lastTickPosX = nbt.getFloat("size");
        radis = (int) nbt.getFloat("radius");
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {

    }

    @Override
    public void writeAdditional(CompoundNBT nbt)
    {
        nbt.putFloat("size", (float) lastTickPosX);
        nbt.putFloat("radius", (float) radis);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return null;
    }


    @Override
    public float getBrightness()
    {
        return 1000F;
    }

    @Override
    public boolean isInRangeToRenderDist(double par1)
    {
        return true;
    }



    public EntityEgoBlast setTime()
    {
        ticksExisted = 920;
        return this;
    }
}
