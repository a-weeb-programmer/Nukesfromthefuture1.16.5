package net.nukesfromthefuture.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.nukesfromthefuture.explosion.ExplosionRay;
import net.nukesfromthefuture.explosion.Generic;
import net.nukesfromthefuture.main.Nukesfromthefuture;
import org.apache.logging.log4j.Level;

public class Blast extends Entity {
    //Strength of the blast
    public int strength;
    //How many rays should be created
    public int count;
    //How many rays are calculated per tick
    public int speed;
    public int length;

    public boolean fallout = true;
    private int falloutAdd = 0;

    ExplosionRay explosion;
    public Blast(EntityType<? extends Blast> type, World world){
        super(type, world);
    }
    public Blast(World p_i1582_1_) {
        super(Nukesfromthefuture.blast, p_i1582_1_);
    }

    public Blast(World world, int strength, int count, int speed, int length) {
        super(Nukesfromthefuture.blast, world);
        this.strength = strength;
        this.count = count;
        this.speed = speed;
        this.length = length;
    }

    @Override
    protected void registerData() {

    }

    @Override
    public void tick() {

        if(strength == 0) {
            this.setDead();
            return;
        }

        if(!world.isRemote && fallout && explosion != null) {
            //RadSaveData data = RadSaveData.getData(worldObj);

            //float radMax = (float) (length / 2F * Math.pow(length, 2) / 35F);
            float radMax = Math.min((float) (length / 2F * Math.pow(length, 1.5) / 35F), 15000);
            //System.out.println(radMax);
            float rad = radMax / 4F;
            //data.incrementRad(worldObj, (int)this.posX, (int)this.posZ, rad, radMax);
        }

        this.world.playSound((PlayerEntity) null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.AMBIENT, 10000.0F, 0.8F + this.rand.nextFloat() * 0.2F);
        if(rand.nextInt(5) == 0)
            this.world.playSound((PlayerEntity) null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.AMBIENT,
        10000.0F, 0.8F + this.rand.nextFloat() * 0.2F);

        Generic.dealDamage(this.world, (int)this.getPosX(), (int)this.getPosY(), (int)this.getPosZ(), this.length * 2);

        if(explosion == null) {
            explosion = new ExplosionRay(world, (int)this.getPosX(), (int)this.getPosY(), (int)this.getPosZ(), this.strength, this.count, this.speed, this.length);

            //MainRegistry.logger.info("START: " + System.currentTimeMillis());

			/*if(!worldObj.isRemote)
				for(int x = (int) (posX - 1); x <= (int) (posX + 1); x++)
					for(int y = (int) (posY - 1); y <= (int) (posY + 1); y++)
						for(int z = (int) (posZ - 1); z <= (int) (posZ + 1); z++)
							worldObj.setBlock(x, y, z, Blocks.air);*/
        }

        //if(explosion.getStoredSize() < count / length) {
        if(!explosion.isAusf3Complete) {
            //if(!worldObj.isRemote)
            //MainRegistry.logger.info(explosion.getStoredSize() + " / " + count / length);
            //explosion.collectTip(speed * 10);
            explosion.collectTipMk4_5(speed * 10);
        } else if(explosion.getStoredSize() > 0) {
            //if(!worldObj.isRemote)
            //MainRegistry.logger.info(explosion.getProgress() + " / " + count / length);
            explosion.processTip(Nukesfromthefuture.mk4.get());
        } else if(fallout) {

            //MainRegistry.logger.info("STOP: " + System.currentTimeMillis());

            //FalloutRain fallout = new FalloutRain(this.worldObj);
            //fallout.posX = this.posX;
            //fallout.posY = this.posY;
            //fallout.posZ = this.posZ;
            //fallout.setScale((int)(this.length * 1.8 + falloutAdd) * 100 / 100);

            //this.worldObj.spawnEntityInWorld(fallout);

            this.setDead();
        } else {
            this.setDead();
        }
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {

    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {

    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return new SSpawnObjectPacket(this);
    }


    public static Blast statFac(World world, int r, double x, double y, double z) {
        if(!world.isRemote)
            Nukesfromthefuture.logger.log(Level.INFO, "[NUKE] Initialized nuclear explosion at " + x + " / " + y + " / " + z + " with strength " + r + "!");
        if(r == 0)
            r = 25;

        r *= 2;

        Blast mk4 = new Blast(world);
        mk4.strength = (int)(r);
        mk4.count = (int)(4 * Math.PI * Math.pow(mk4.strength, 2) * 25);
        mk4.speed = (int)Math.ceil(100000 / mk4.strength);
        mk4.setPosition(x, y, z);
        mk4.length = mk4.strength / 2;
        return mk4;
    }

    public static Blast statFacExperimental(World world, int r, double x, double y, double z) {

        if(!world.isRemote)
            Nukesfromthefuture.logger.log(Level.INFO, "[NUKE] Initialized eX explosion at " + x + " / " + y + " / " + z + " with strength " + r + "!");

        r *= 2;

        Blast mk4 = new Blast(world);
        mk4.strength = (int)(r);
        mk4.count = (int)(4 * Math.PI * Math.pow(mk4.strength, 2) * 25);
        mk4.speed = (int)Math.ceil(100000 / mk4.strength);
        mk4.setPosition(x, y, z);
        mk4.length = mk4.strength / 2;
        return mk4;
    }

    public static Blast statFacNoRad(World world, int r, double x, double y, double z) {

        if(!world.isRemote)
            Nukesfromthefuture.logger.log(Level.INFO, "[NUKE] Initialized nR explosion at " + x + " / " + y + " / " + z + " with strength " + r + "!");

        r *= 2;

        Blast mk4 = new Blast(world);
        mk4.strength = (int)(r);
        mk4.count = (int)(4 * Math.PI * Math.pow(mk4.strength, 2) * 25);
        mk4.speed = (int)Math.ceil(100000 / mk4.strength);
        mk4.setPosition(x, y, z);
        mk4.length = mk4.strength / 2;
        mk4.fallout = false;
        return mk4;
    }

    public Blast moreFallout(int fallout) {
        falloutAdd = fallout;
        return this;
    }
}
