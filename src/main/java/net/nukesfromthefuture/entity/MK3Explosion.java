package net.nukesfromthefuture.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.INBTType;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;
import net.nukesfromthefuture.explosion.Advanced;
import net.nukesfromthefuture.explosion.Generic;
import net.nukesfromthefuture.main.Nukesfromthefuture;
import net.nukesfromthefuture.saveddata.RadSavedData;
import org.apache.logging.log4j.Level;

public class MK3Explosion extends Entity{
    public int age = 0;
    public int destructionRange = 0;
    public Advanced exp;
    public Advanced wst;
    public Advanced vap;
    public double x = getPosX();
    public double y = getPosY();
    public double z = getPosZ();
    public int speed = 1;
    public float coefficient = 1;
    public float coefficient2 = 1;
    public boolean did = false;
    public boolean did2 = false;
    public boolean waste = true;
    //Extended Type
    public int extType = 0;
    public MK3Explosion(EntityType<? extends MK3Explosion> type, World world){
        super(type, world);
    }
    //not great, but deffinately better than having to call and initialize the variables individually
    public MK3Explosion(World p_i1582_1_, int x, int y, int z, int strength, int speed) {
        super(Nukesfromthefuture.generic, p_i1582_1_);
        this.x = x;
        this.y = y;
        this.z = z;
        destructionRange = strength;
        this.speed = speed;
        setPosition(x, y, z);
        ignoreFrustumCheck = true;
    }
    /**Don't use this one, It's a depriciated constructor that I was too lazy to delete*/
    @Deprecated
    public MK3Explosion(World world){
        super(Nukesfromthefuture.generic, world);
    }
    @Override
    public void read(CompoundNBT nbt) {
        age = nbt.getInt("age");
        destructionRange = nbt.getInt("destructionRange");
        speed = nbt.getInt("speed");
        coefficient = nbt.getFloat("coefficient");
        coefficient2 = nbt.getFloat("coefficient2");
        did = nbt.getBoolean("did");
        did2 = nbt.getBoolean("did2");
        waste = nbt.getBoolean("waste");
        extType = nbt.getInt("extType");

        long time = nbt.getLong("milliTime");


        if(this.waste)
        {
            exp = new Advanced((int)this.x, (int)this.y, (int)this.z, this.world, this.destructionRange, this.coefficient, 0);
            exp.readFromNbt(nbt, "exp_");
            wst = new Advanced((int)this.x, (int)this.y, (int)this.z, this.world, (int)(this.destructionRange * 1.8), this.coefficient, 2);
            wst.readFromNbt(nbt, "wst_");
            vap = new Advanced((int)this.x, (int)this.y, (int)this.z, this.world, (int)(this.destructionRange * 2.5), this.coefficient, 1);
            vap.readFromNbt(nbt, "vap_");
        }

        this.did = true;

    }

    @Override
    protected void readAdditional(CompoundNBT compound) {

    }

    @Override
    protected void writeAdditional(CompoundNBT nbt) {
        nbt.putInt("age", age);
        nbt.putInt("destructionRange", destructionRange);
        nbt.putInt("speed", speed);
        nbt.putFloat("coefficient", coefficient);
        nbt.putFloat("coefficient2", coefficient2);
        nbt.putBoolean("did", did);
        nbt.putBoolean("did2", did2);
        nbt.putBoolean("waste", waste);
        nbt.putInt("extType", extType);

        nbt.putLong("milliTime", System.currentTimeMillis());

        if(exp != null)
            exp.saveToNbt(nbt, "exp_");
        if(wst != null)
            wst.saveToNbt(nbt, "wst_");
        if(vap != null)
            vap.saveToNbt(nbt, "vap_");


    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return new SSpawnObjectPacket(this);
    }



    @Override
    protected void registerData() {

    }

    @Override
    public void tick() {
        super.tick();
        if(!world.isRemote && age == 1){
            Nukesfromthefuture.logger.log(Level.INFO, "Beta nuclear explosion has been done successfully at: " + x + ", " + y + ", " + z + "." + " With a strength of:" + destructionRange );
        }
        if(!world.isRemote && waste) {
            RadSavedData data = RadSavedData.getData((ServerWorld) world);

            //float radMax = (float) (length / 2F * Math.pow(length, 2) / 35F);
            float radMax = Math.min((float) ((destructionRange / 2) / 2F * Math.pow(destructionRange / 2, 1.5) / 35F), 15000);
            //System.out.println(radMax);
            float rad = radMax / 4F;
            data.incrementRad(world, (int)this.getPosX(), (int)this.getPosY(), rad, radMax, destructionRange * 1.5, getPosX(), getPosZ());
        }
        if(!world.isRemote && !this.did)
        {


            if(this.waste)
            {
                exp = new Advanced((int)this.x, (int)this.y, (int)this.z, this.world, this.destructionRange, this.coefficient, 0);
                wst = new Advanced((int)this.x, (int)this.y, (int)this.z, this.world, (int)(this.destructionRange * 1.8), this.coefficient, 2);
                vap = new Advanced((int)this.x, (int)this.y, (int)this.z, this.world, (int)(this.destructionRange * 2.5), this.coefficient, 1);
            }

            this.did = true;
        }

        speed += 1;	//increase speed to keep up with expansion

        boolean flag = false;
        boolean flag3 = false;
        if(!world.isRemote) {
            for (int i = 0; i < this.speed; i++) {
                if (waste) {
                    flag = exp.update();
                    wst.update();
                    flag3 = vap.update();

                    if (flag3) {
                        this.setDead();
                    }
                }
            }
        }
        if(!flag) {
            this.world.playSound((PlayerEntity) null, this.x, this.y, this.z, SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.AMBIENT, 10000.0F, 0.8F + this.rand.nextFloat() * 0.2F);

            if (waste || extType != 1) {
                Generic.dealDamage(this.world, (int) this.x, (int) this.y, (int) this.z, this.destructionRange * 2);
            } else {
                //DedRad.doRadiation(worldObj, posX, posY, posZ, 15000, 250000, this.destructionRange);
            }
        } else {
            if (!did2 && waste) {
                //FalloutRain fallout = new FalloutRain(this.worldObj, (int)(this.destructionRange * 1.8) * 10);
                //fallout.posX = this.posX;
                //fallout.posY = this.posY;
                //fallout.posZ = this.posZ;
                //fallout.setScale((int)(this.destructionRange * 1.8));

                //this.worldObj.spawnEntityInWorld(fallout);
                //this.worldObj.getWorldInfo().setRaining(true);

                did2 = true;
            }
        }

        age++;
    }

}
